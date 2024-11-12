package com.shopme.admin.user.exporter;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.user.AbstractExporter;
import com.shopme.common.entity.Users;

public class UserCsvExporter extends AbstractExporter {
	public void export(List<Users> lstUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "users_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"User ID", "Email", "First Name", "Last Name","Roles","Enable"};
		String[] csvItems = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		csvWriter.writeHeader(csvHeader);
		for(Users users : lstUsers) {
			csvWriter.write(users, csvItems);
		}
		csvWriter.close();
	}
}
