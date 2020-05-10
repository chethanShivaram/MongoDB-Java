package com.jcg.java.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.mongodb.MongoClient;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JavaMongoDbDemo {

	public static void main(String[] args) throws ParseException, java.text.ParseException {

		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoDatabase db = mongo.getDatabase("EIP");
		MongoCollection<Document> collection = db.getCollection("timesheet");
//		MongoCollection<Document> collections = db.getCollection("unfreezedList");
		Date date = new Date();

		for (int i = 1; i <= 3000; i++) {
			Date daysA = new DateTime(date).minusDays(i).toDate();
			SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd");
			String getStringDate = formatt.format(daysA);
			Calendar test = Calendar.getInstance();
			test.setTime(formatt.parse(getStringDate));
			// System.out.println("user"+i+" " +getStringDate +" "+ "Month "+
			// test.get(Calendar.MONTH));
			int dateNo = test.get(Calendar.DATE);

			if ((dateNo >= 1) && (dateNo <= 26)) {
				Document document = new Document();
				document.put("user_name", "user" + i);
				document.put("email_id", "user1@gmail.com");
				document.put("save_date", new Date());
				document.put("submit_date", new Date());
				document.put("employee_id", "user" + i);
				if (i % 3 == 0) {
					document.put("approve_status", "Pending");
				} else if (i % 2 == 0) {
					document.put("approve_status", "Approved");
				} else {
					document.put("approve_status", "Rejected");
				}

				List<Document> docs = new ArrayList<Document>();
				int dat = 21;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

				Calendar c = Calendar.getInstance();
				Date startDate = formatter
						.parse(c.get(Calendar.YEAR) + "-" + ((c.get(Calendar.MONTH)) - 1) + "-" + dat);
				Date endDate = formatter.parse(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)) + "-" + dat);
				Calendar start = Calendar.getInstance();
				start.setTime(startDate);
				Calendar end = Calendar.getInstance();
				end.setTime(endDate);

				for (Date dates = start.getTime(); start.before(end); start.add(Calendar.DATE,
						1), dates = start.getTime()) {
					Document temp = new Document();
					SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
					String dayName = simpleDateformat.format(dates);
					if (dayName.equals("Mon") || dayName.equals("Tue")) {
						temp.put("status", "Present");
					} else if (dayName.equals("Sat") || dayName.equals("Sun")) {
						temp.put("status", "Holiday");
					} else if (dayName.equals("Wed")) {
						temp.put("status", "AL");
					} else if (dayName.equals("Thu")) {
						temp.put("status", "SL");
					} else {
						temp.put("status", "WFH");
					}
					// Date daysB = new DateTime(date).minusDays(j).toDate();
					temp.put("date_time", dates);
					docs.add(temp);
				}
				document.put("timesheet_date_status", docs);
				collection.insertOne(document);
			}
		}
	}
}