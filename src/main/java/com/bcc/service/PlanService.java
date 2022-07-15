package com.bcc.service;

import java.util.List;
import java.util.Map;

import com.bcc.domain.BoardVO;
import com.bcc.domain.GrpAcceptVO;
import com.bcc.domain.HotelVO;
import com.bcc.domain.MemberVO;
import com.bcc.domain.PlanMemberVO;
import com.bcc.domain.PlanVO;

public interface PlanService {

	// 아이디 가진 회원 목록 가져오기
	public List<MemberVO> getMemberList(String id);
	
	// 그룹 번호 계산
	public Integer getGrpNum();

	// 그룹 생성
	public void createGrp(PlanVO vo);

	// 회원 라이센스 가져오기
	public String getLicense(String id);

	// 회원 초대받은 목록 
	public List<GrpAcceptVO> getGrpAcceptList(String receiver);

	// 회원이 소속된 그룹 정보 저장
	public void insertGrpMember(PlanMemberVO member);

	// 그룹 초대 수락 리스트에서 제거
	public void deleteInvitation(GrpAcceptVO vo);

	// 소속된 그룹 번호 가져오기
	public List<PlanMemberVO> getGrpList(String id);

	// 그룹 이름 가져오기
	public String getGrpName(int num);

	// 그룹의 멤버 리스트 가져오기
	public List<MemberVO> getGrpMemberList(int grp_num);

	// 플랜 멤버 삭제
	public void delPlanMem(PlanMemberVO vo);

	// 방장 정보 가져오기
	public String getLeader(int num);

	// 해당 그룹의 초대 중인 멤버
	public List<GrpAcceptVO> getInvitingList(int grp_num);

	// 그룹에 멤버 초대
	public void inviteMember(GrpAcceptVO vo);

	// 회원 이름 가져오기
	public String getName(String id);

	// 초대 취소
	public void inviteCancle(GrpAcceptVO vo);

	// 그룹 회원인지 확인
	public int checkGrpMember(PlanMemberVO vo);

	// 다음 방장 아이디 가져오기
	public String getNextLeader(int grp_num);

	// 방장 새로 설정
	public void updateLeader(PlanVO plan);

	// 전체 관광지 정보 가져오기
	public List<BoardVO> getTourList();

	// 전체 맛집 정보 가져오기
	public List<BoardVO> getRestaurantList();

	// 숙소 리스트 가져오기
	public List<HotelVO> getHotelList();

	// 카테고리별 검색 결과 가져오기
	public List<Object> getSearchList(String category, String keyword, List<HotelVO> hotellist);

	// 여행 경로 저장
	public void modifyPlan(PlanVO vo);

	// 여행 경로 목록 가져오기
	public List<List<Object>> getPlanList(int num, List<HotelVO> hotellist);

}
