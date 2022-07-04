package com.cloudaxis.agsc.portal.helpers;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cloudaxis.agsc.portal.constants.CandidateProfileConstants;
import com.cloudaxis.agsc.portal.model.Caregiver;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class ExportCaregiverlUtils {

	protected Logger logger = Logger.getLogger(ExportCaregiverlUtils.class);

	public void exportCaregiverToExcel(HttpServletResponse response, List<Caregiver> caregiverList) {
		try {
			// format output stream
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(CandidateProfileConstants.DEFALUT_FILE_NAME.getBytes("GBK"), "iso8859-1") + ".xls");
			response.setContentType("application/msexcel");
			
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());

			// define a workbook
			HSSFWorkbook workbook = new HSSFWorkbook();
			// create a sheet
			HSSFSheet sheet = workbook.createSheet(CandidateProfileConstants.DEFALUT_SHEET_NAME);
			sheet.setDefaultColumnWidth((short) 15);

			// set sheet header
			setSheetHeader(workbook, sheet);

			// set sheet date
			setSheetDate(sheet, caregiverList);

			workbook.write(outputStream);
			workbook.close();
		} catch (Exception e) {
			logger.error("Export Excel failed.", e);
		}
	}

	private void setSheetHeader(HSSFWorkbook workbook, HSSFSheet sheet) {
		// create Header
		HSSFRow headerRow = sheet.createRow(0);

		// create Header style
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);

		// set header value
		String[] headers = CandidateProfileConstants.EXCEL_HEADERS;
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = headerRow.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
	}

	private void setSheetDate(HSSFSheet sheet, List<Caregiver> caregiverList) {
		if (CollectionUtils.isNotEmpty(caregiverList)) {
			for (int i = 0; i < caregiverList.size(); i++) {
				// createRow(i + 1) : the first row use as header
				HSSFRow row = sheet.createRow(i + 1);
				Caregiver caregiver = caregiverList.get(i);
				
				int index = 0;
				row.createCell(index++).setCellValue(caregiver.getFullName());
				row.createCell(index++).setCellValue(caregiver.getEmail());
				row.createCell(index++).setCellValue(caregiver.getMobile());
				row.createCell(index++).setCellValue(caregiver.getResumatorStatus());
				// work in HK and SG
				String work_in_space = getWorkInSpace(caregiver);
				row.createCell(index++).setCellValue(work_in_space);
				row.createCell(index++).setCellValue(caregiver.getWorkInSG());
				row.createCell(index++).setCellValue(caregiver.getWorkInHK());
				row.createCell(index++).setCellValue(caregiver.getWorkInTW());
				row.createCell(index++).setCellValue(caregiver.getSkype());
				row.createCell(index++).setCellValue(caregiver.getGender());
				if(caregiver.getDateOfBirth() != null) {
					row.createCell(index++).setCellValue(caregiver.getDateOfBirth().toString());
				}
				row.createCell(index++).setCellValue(caregiver.getCountryOfBirth());
				/** -----  newly added ------- */
				row.createCell(index++).setCellValue(caregiver.getAge());
				row.createCell(index++).setCellValue(caregiver.getDateApplied().toString());
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfPlacement())); 
				row.createCell(index++).setCellValue(caregiver.getNumbersOfPlacement()); 
				row.createCell(index++).setCellValue(ApplicationDict.getSelectstatus().get(caregiver.getStatus()+""));
				row.createCell(index++).setCellValue(caregiver.getRegisteredConcorde());
				row.createCell(index++).setCellValue(caregiver.getPreDeployment());  
				row.createCell(index++).setCellValue(caregiver.getMedicalCertVerified());
				row.createCell(index++).setCellValue(CandidateProfileConstants.EMPTY_DISPLAY); // FOR HOW LONG WOULD YOU LIKE TO WORK AS A LIVE-IN CAREGIVER WITH ACTIVE GLOBAL?
				/** ------   end  -------- */
				row.createCell(index++).setCellValue(caregiver.getNearestAirport());
				row.createCell(index++).setCellValue(caregiver.getNationality());
				row.createCell(index++).setCellValue(caregiver.getCurrentAddress());
				row.createCell(index++).setCellValue(caregiver.getHeight());
				row.createCell(index++).setCellValue(caregiver.getWeight());
				row.createCell(index++).setCellValue(caregiver.getMaritalStatus());
				row.createCell(index++).setCellValue(caregiver.getHasChildren());
				row.createCell(index++).setCellValue(caregiver.getChildrenNameAge());
				row.createCell(index++).setCellValue(caregiver.getSiblings());
				row.createCell(index++).setCellValue(caregiver.getLanguages());
				row.createCell(index++).setCellValue(caregiver.getReligion());
				row.createCell(index++).setCellValue(caregiver.getFoodChoice());
				row.createCell(index++).setCellValue(caregiver.getHoldPassport());
				row.createCell(index++).setCellValue(caregiver.getEducation());
				row.createCell(index++).setCellValue(caregiver.getYearGraduation());
				row.createCell(index++).setCellValue(caregiver.getYearStudies());
				row.createCell(index++).setCellValue(caregiver.getEducationalLevel());
				row.createCell(index++).setCellValue(caregiver.getExp());
				row.createCell(index++).setCellValue(caregiver.getCertifiedCpr());
				row.createCell(index++).setCellValue(caregiver.getCaregiverBeforeExp());
				row.createCell(index++).setCellValue(caregiver.getWorkedInSG());
				row.createCell(index++).setCellValue(caregiver.getSgFin());
				row.createCell(index++).setCellValue(caregiver.getLocation());
				row.createCell(index++).setCellValue(caregiver.getSpecialities());
				row.createCell(index++).setCellValue(caregiver.getCurrentJob());
				row.createCell(index++).setCellValue(caregiver.getCurrentJobTime());
				row.createCell(index++).setCellValue(caregiver.getMotivation());
				row.createCell(index++).setCellValue(caregiver.getAvailability());
				row.createCell(index++).setCellValue(caregiver.getAllergies());
				row.createCell(index++).setCellValue(caregiver.getDiagnosedConditions());
				row.createCell(index++).setCellValue(caregiver.getHistoryOfTreatment());
				row.createCell(index++).setCellValue(caregiver.getHobbies());
				row.createCell(index++).setCellValue(caregiver.getTesdaNcii());
				row.createCell(index++).setCellValue(caregiver.getInterviewTime());
				row.createCell(index++).setCellValue(caregiver.getReferrer());
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getMarkedRegisteredWithConcorde()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getMarkedAdvancedPlacementScheme()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getMarkedMedicalCertVerify()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateApplied()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfAwaitingDocument()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfSchedulingInterview()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfInterviewScheduled()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfShortlisted()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfNotSelected()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfShortlistedWithDiffAvail()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfReadyForPlacement()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfTagged()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfPlacement()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfOnHold()));
				row.createCell(index++).setCellValue(DateUtil.trimDateForExcel(caregiver.getDateOfBlacklisted()));
				row.createCell(index++).setCellValue(caregiver.getPhotoPassport());
				row.createCell(index++).setCellValue(caregiver.getPhotoDegrees());
				row.createCell(index++).setCellValue(CandidateProfileConstants.EMPTY_DISPLAY);
				row.createCell(index++).setCellValue(CandidateProfileConstants.EMPTY_DISPLAY);
				row.createCell(index++).setCellValue(caregiver.getPhoto());
				row.createCell(index++).setCellValue(caregiver.getOtherFiles());
				row.createCell(index++).setCellValue(CandidateProfileConstants.EMPTY_DISPLAY);
				row.createCell(index++).setCellValue(CandidateProfileConstants.EMPTY_DISPLAY);
				row.createCell(index++).setCellValue(CandidateProfileConstants.EMPTY_DISPLAY);
			}
		}

	}

	private String getWorkInSpace(Caregiver caregiver) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean isWorkInHK = "Yes".equals(caregiver.getWorkInHK());
		if (isWorkInHK) {
			stringBuilder.append("Hong Kong");
		}
		if ("Yes".equals(caregiver.getWorkInSG())) {
			if (isWorkInHK) {
				stringBuilder.append(", ");
			}
			stringBuilder.append("Singapore");
		}
		return stringBuilder.toString();
	}
}
