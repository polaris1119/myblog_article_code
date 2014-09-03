// Copyright 2014 The StudyGolang Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.
// http://studygolang.com
// Author：polaris	studygolang@gmail.com

// 使用 Bitmap 实现排重
// Usage taskdiff destfile onefile twofile ...

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<ctype.h>
#include "bitmap.h"

void write2bitmap(Bitmap* bitmap, const char* filename, uint8_t value)
{
	FILE* fh = fopen(filename, "r");

	char* line;
	char* pline = malloc(sizeof(line));

	while(fgets(line, 1024, fh) != NULL) {
		size_t len = strlen(line);
		if (isspace(line[len-1])) {
			strncpy(pline, line, len-1);
		} else {
			strcpy(pline, line);
		}
		uint64_t num = (uint64_t)atoll(pline);
		set_bit(bitmap, num, value);
	}

	free(pline);

	fclose(fh);
}

int main(int argc, char* argv[])
{
	if (argc < 2) {
		printf("Usage %s [taskid] [otherid...] \n", argv[0]);
		exit(1);
	}
	Bitmap* bitmap = init_bitmap();

	write2bitmap(bitmap, argv[1], 1);
	
	int i;
	for (i = 2; i < argc; i++) {
	}

	char* newFile = malloc(sizeof(argv[1]));
	strcpy(newFile, argv[1]);
	
	FILE* fh = fopen(strcat(newFile, "_diff"), "w");
	uint64_t offset;
	for (offset = 0; offset < bitmap->maxpos+1; offset++) {
		if (get_bit(bitmap, offset)) {
			fprintf(fh, "%u\n", offset);
		}
	}

	fclose(fh);
	
	free(newFile);

	free_bitmap(bitmap);

	return EXIT_SUCCESS;
}
