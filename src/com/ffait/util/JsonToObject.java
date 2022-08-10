package com.ffait.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import com.ffait.pass.ExamResult;

public class JsonToObject {
	
	//获取ExamResult的List对象
	public static List<ExamResult> getExamresult (String str) throws Exception {
        Gson gson=new Gson();
        List<ExamResult> examResultList = gson.fromJson(str,new TypeToken<List<ExamResult>>() {}.getType());
        return examResultList;

    }

}
