
package com.cloudaxis.agsc.portal.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudaxis.agsc.portal.model.EvaluationQuestion;
import com.cloudaxis.agsc.portal.service.AssessmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/assessment")
public class AssessmentController extends AbstractController {

	@Autowired
	private AssessmentService evaluationService;
	
	public AssessmentController() {
		super(AssessmentController.class);
	}
	
	@RequestMapping(value="/getEvaluationQuestionListByTemplateId", method = RequestMethod.POST)
	public void getEvaluationQuestionListByTemplateId(@RequestParam(value="templateId") String templateId, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, IOException{
		List<EvaluationQuestion> questionList = evaluationService.getEvaluationQuestionListByTemplateId(Integer.parseInt(templateId));
		ObjectMapper mapper = new ObjectMapper();
		response.setCharacterEncoding("utf-8");
		String json = mapper.writeValueAsString(questionList);  
		response.getWriter().print(json);
	}
	
	@RequestMapping(value="/saveEvaluationAnswerList", method = RequestMethod.POST)
	public String saveEvaluationAnswerList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		String applicantId = request.getParameter("caregiverId");
		String evaluatedId = request.getParameter("evaluatedId");
		String templateId = request.getParameter("templateId");
		String summary = request.getParameter("evaluationSummary");
		String[] questionIdArr = request.getParameterValues("questionId");
		String[] commentArr = request.getParameterValues("evaluationComment");
		String[] selectionArr = getSelectionArrByRequest(questionIdArr, request);
		evaluationService.saveEvaluationAnswerList(applicantId, evaluatedId, templateId, questionIdArr, selectionArr, commentArr, summary);

		return "redirect:/dashboard/getCandidate?userId=" + Integer.parseInt(applicantId) + "&active=" + 3;
	}

	private String[] getSelectionArrByRequest(String[] questionIdArr, HttpServletRequest request) {
		String[] selectionArr = new String[questionIdArr.length];
		for(int i = 0; i < selectionArr.length; i++){
			String selection = request.getParameter("evaluationScore" + questionIdArr[i]);
			selectionArr[i] = selection;
		}
		return selectionArr;
	}
	
	@RequestMapping(value="/getEvaluationTemplateIdsByApplicationId", method = RequestMethod.POST)
	public void getEvaluationTemplateIdsByApplicationId(@RequestParam("applicantId") String applicantId, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, IOException{
		List<Integer> templateIds = evaluationService.getEvaluationTemplateIdsByApplicationId(applicantId);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(templateIds);  
		response.getWriter().print(json);
	}
	
	@RequestMapping(value="deleteEvaluationByApplicantIdAndTemplateId", method = RequestMethod.GET)
	public void deleteEvaluationByApplicantIdAndTemplateId(@RequestParam("applicantId") String applicantId, @RequestParam("templateId") String templateId, HttpServletRequest request, HttpServletResponse response){
		evaluationService.deleteEvaluationByApplicantIdAndTemplateId(applicantId, templateId);
	}
	
}
