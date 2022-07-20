package com.bcc.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bcc.domain.BoardVO;
import com.bcc.domain.PlanVO;

@Repository
public class PlanDAOImpl implements PlanDAO {

	private static final Logger log = LoggerFactory.getLogger(PlanDAOImpl.class);

	@Inject
	private SqlSession sqlSession;

	private static final String NAMESPACE = "com.bcc.mapper.planMapper";

	@Override
	public void createPlan(PlanVO vo) {
		sqlSession.insert(NAMESPACE + ".createPlan", vo);
	}

	@Override
	public List<PlanVO> getPlanList(String id) {
		return sqlSession.selectList(NAMESPACE + ".getPlanList", id);
	}

	@Override
	public List<BoardVO> getTourList(int t_category) {
		return sqlSession.selectList(NAMESPACE + ".getTourList", t_category);
	}

	@Override
	public List<BoardVO> getTourSearch(BoardVO vo) {
		return sqlSession.selectList(NAMESPACE + ".getTourSearch", vo);
	}

	@Override
	public BoardVO getTourInfo(int num) {
		return sqlSession.selectOne(NAMESPACE + ".getTourInfo", num);
	}

	@Override
	public void modifyTourPlan(PlanVO vo) {
		sqlSession.update(NAMESPACE + ".modifyTourPlan", vo);
	}

	@Override
	public PlanVO getPlanInfo(int num) {
		return sqlSession.selectOne(NAMESPACE + ".getPlanInfo", num);
	}

	@Override
	public void delPlan(int num) {
		sqlSession.delete(NAMESPACE + ".delPlan", num);
	}
}
