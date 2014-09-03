package com.studygolang.bitmap.taskdiff;

import com.studygolang.bitmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TaskDiff {
	public static void main(String[] argv) throws Exception {
		
		long start = System.currentTimeMillis();

		if (argv.length < 1) {
			System.err.println("Usage java -jar test.jar [taskid] [otherid]");
			System.exit(1);
		}
		Bitmap bitmap = new Bitmap();

		write2bitmap(bitmap, argv[0], 1);

		long curSetTime = System.currentTimeMillis();
		System.out.println("main file read spend time:" + (curSetTime - start)
				/ 1000);
		
		for (int i = 1; i < argv.length; i++) {
			write2bitmap(bitmap, argv[i], 0);
		}
		
		System.out.println("diff spend time:" + (System.currentTimeMillis() - curSetTime)
				/ 1000);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(argv[0] + "_diff")));
		
		for (long offset = 0; offset < bitmap.getMaxpos()+1; offset++) {
			if (bitmap.getBit(offset) == 1) {
				bw.write(offset+"\n");
			}
		}
		
		bw.flush();
		bw.close();
		
		System.out.println("total spend time:" + (System.currentTimeMillis() - start)
				/ 1000);
	}

	public static void write2bitmap(Bitmap bitmap, String filename, int value)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename)));

		String line = "";
		while ((line = br.readLine()) != null) {
			if (line == "") {
				continue;
			}
			
			long offset = Long.parseLong(line.trim());
			bitmap.setBit(offset, value);
		}

		br.close();
	}
}