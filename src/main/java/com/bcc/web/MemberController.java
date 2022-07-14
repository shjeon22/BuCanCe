package com.bcc.web;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bcc.domain.BoardVO;
import com.bcc.domain.Criteria;
import com.bcc.domain.MemberVO;
import com.bcc.domain.PageMaker;
import com.bcc.domain.SearchCriteria;
import com.bcc.service.BoardService;
import com.bcc.service.KakaoService;
import com.bcc.service.MemberService;
import com.bcc.service.TourService;

@Controller
public class MemberController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@Inject	
	private MemberService service;
	
	@Inject
	private BoardService bs;
	
	@Inject
	private TourService ts;
	
	// http://localhost:8088/index
		@RequestMapping(value = "/index", method = RequestMethod.GET)
		public String indexGET() {
			log.info(" insertGET() 호출 ");
			log.info(" view 페이지 출력 -> 정보 입력 ");
			
			return "/member/index";
		}

		// 카테고리 화면 변환
		@RequestMapping(value = "/ajaxListAll", method = RequestMethod.GET)
		public @ResponseBody List ajaxListAll(SearchCriteria scri) throws Exception {
			List boardList = bs.list(scri);

			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(scri);
			pageMaker.setTotalCount(bs.listCount(scri));
			boardList.add(pageMaker);

			return boardList;
		}

		@RequestMapping(value = "/ajaxListByCategory", method = RequestMethod.GET)
		public @ResponseBody List<BoardVO> ajaxListByCategory(@RequestParam(value = "category") String category,
				Model model) throws Exception {
			List boardList = bs.category(category);

			PageMaker pageMaker = new PageMaker();
			if (boardList != null) {
				pageMaker.setCri(new Criteria());
				pageMaker.setTotalCount(boardList.size());
			}
			boardList.add(pageMaker);
			return boardList;
		}

		// 글 전체 목록
		// http://localhost:8088/index
		// http://localhost:8088/favorite
		@RequestMapping(value = "/favorite", method = RequestMethod.GET)
		public String listAll(@ModelAttribute("result") String result, SearchCriteria scri, MemberVO vo, Model model, HttpSession session)
				throws Exception {
			
			// 조회수
			session.setAttribute("upFlag", "1");
			
			// 글 정보를 가지고 오기
			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(scri);
			pageMaker.setTotalCount(bs.listCount(scri));

//			log.info(pageMaker+"");
//			model.addAttribute("pageMaker", pageMaker);
//			model.addAttribute("boardList", bs.list(scri));
			
			log.info(pageMaker+"");
			model.addAttribute("pageMaker", pageMaker);
			model.addAttribute("boardList", bs.list(scri));
			
			return "/member/favorite";

		}

		
		// http://localhost:8088/insert
		// 회원가입
		@RequestMapping(value = "/insert", method = RequestMethod.GET)
		public String insertGET() {
			log.info(" insertGET() 호출 ");
			log.info(" view 페이지 출력 -> 정보 입력 ");
			
			return "/member/insertForm";
		}
		
		@RequestMapping(value = "/insert", method = RequestMethod.POST)
		public String insertPOST(MemberVO vo, HttpServletRequest request) throws Exception {
			
			log.info(" insertPOST() 호출 ");
			log.info(vo+"");
			
			// DB에 회원정보 저장 => 서비스 계층 호출 => DAO 호출
			// DAO 객체 주입? (동작가능) => 강한결합 (종속적인 상황이 발생)
			service.memberInsert(vo);
			
			log.info("회원가입 완료 -> 로그인 페이지 이동");
			
			// 페이지 이동( 로그인페이지 )
			return "redirect:/login";
			
		}
		
		// http://localhost:8088/login
		// 로그인 /login
		@RequestMapping(value = "/login",method = RequestMethod.GET)
		public String loginGet() {
			log.info("loginGet() 호출");
			log.info(" /member/loginForm.jsp 페이지로 이동");
			
			return "/member/loginForm";
		}
		
		@RequestMapping(value = "/login",method = RequestMethod.POST)
		public String loginPOST(MemberVO vo,HttpSession session) {
			log.info("loginPOST() 호출");

			log.info(vo+"");
			
			// db동작 호출을 위해서 서비스 동작을 호출 - loginCheck()
			MemberVO resultVO = service.loginCheck(vo);
			// 로그인 실패 - 페이지 이동(로그인페이지)
			if(resultVO == null) {
				log.info("로그인 정보 없음! 페이지 이동 ");
				
				return "redirect:/login";
			}
			
			// 로그인 성공 - 아이디를 세션객체에 저장
			session.setAttribute("id", resultVO.getId());
			
			log.info(" 로그인 성공 ");
			
			// 메인페이지로 이동
			return "redirect:/index";
		}
		
		// http://localhost:8088/logout
		// 로그아웃
		@RequestMapping(value = "/logout",method = RequestMethod.GET)
		public String logoutGET(HttpSession session) {
			log.info(" logoutGET() 호출 ");
			
			// 로그아웃 => 세션정보 초기화
			session.invalidate();
			
			// 페이지이동
			return "redirect:/index";
		}
		
		// http://localhost:8088/mypage
		// 마이페이지
		@RequestMapping(value = "/mypage",method = RequestMethod.GET)
		public String mypageGET(MemberVO vo,HttpSession session) {
			log.info(" mypageGET() 호출 ");
			log.info(" view 페이지 출력 -> 정보 입력 ");
			
			// 페이지이동
			return "/member/mypageForm";
		}
		
		// http://localhost:8088/update
		// 회원정보 수정
		@RequestMapping(value = "/update",method = RequestMethod.GET)
		public String updateGET(HttpSession session,Model model) {
			log.info(" updateGET() 호출 ");
			
			String id = (String)session.getAttribute("id");

			model.addAttribute(service.getMember(id));
			
			return "/member/updateForm";
		}
		
		// 회원정보 수정
		@RequestMapping(value = "/update", method = RequestMethod.POST)
		public String updatePOST(HttpSession session, MemberVO vo) {
			
			String id = (String)session.getAttribute("id");
			
			// 서비스 - 정보 수정동작 호출
			int result = service.updateMember(vo);
			
			if(result != 1) {
				return "redirect:/update";
			}
			
			// 페이지 이동
			return "redirect:/mypage";
		}


		@RequestMapping(value = "/liUp", method = RequestMethod.POST)
		public String liUpPOST(HttpSession session, String license) {
			
			log.info(" liUpPOST() 호출 ");
			
			String id = (String)session.getAttribute("id");
			
			service.liUp(id);
			
			return "redirect:/update";
		}
		
		// http://localhost:8088/login
		@RequestMapping(value = "/liDown", method = RequestMethod.POST)
		public String liDownPOST(HttpSession session, String license) {
			
			log.info(" liDownPOST() 호출 ");
			
			String id = (String)session.getAttribute("id");
			
			service.liDown(id);
			
			return "redirect:/update";
		}

		// 회원정보 삭제(탈퇴)
		@RequestMapping(value = "/delete", method = RequestMethod.GET)
		public String deleteGET() {
			
			log.info(" deleteGET() 호출 ");
			
			return "/member/deleteForm";
		}
		
		// 회원정보 삭제(탈퇴)
		@RequestMapping(value = "/delete", method = RequestMethod.POST)
		public String deletePOST(HttpSession session,MemberVO vo/* @ModelAttribute("pw") String pw, */) {
			log.info(" deletePOST() 호출 ");
			//log.info(pw);
			vo.setId((String)session.getAttribute("id"));
			log.info(vo+"");
			
			// 서비스 - 회원삭제동작
			service.deleteMember(vo);
			
			// 회원정보(세션) 초기화
			session.invalidate();
			
			// 페이지 이동
			return "redirect:/index";
		}

	    // 아이디 체크

	    @PostMapping("/idCheck")
	    @ResponseBody
	    public int idCheck(@RequestParam("id") String id){
	        log.info("userIdCheck 진입");
	        log.info("전달받은 id:"+id);
	        int cnt = service.idCheck(id);
	        log.info("확인 결과:"+cnt);
	        
	        return cnt;
	    }

	    @Inject
	    private KakaoService ks;

		// http://localhost:8088/login
		// http://localhost:8088/kakao_login 
		@RequestMapping(value="/kakao_login", method=RequestMethod.GET)
		public String kakao_login(@RequestParam(value = "code", required = false) String code) throws Exception {
			System.out.println("#########" + code);
			String access_Token = ks.getAccessToken(code);
			HashMap<String, Object> userInfo = ks.getUserInfo(access_Token);
			System.out.println("###access_Token#### : " + access_Token);
			System.out.println("###nickname#### : " + userInfo.get("nickname"));
			System.out.println("###email#### : " + userInfo.get("email"));

			return "/member/loginForm";
			
	    	}
		
}