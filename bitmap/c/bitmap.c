// Copyright 2014 The StudyGolang Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.
// http://studygolang.com
// Author：polaris	studygolang@gmail.com

#include<stdlib.h>
#include "bitmap.h"

/* 暂时只支持 1 << 32 位*/
#define BITMAP_SIZE (1ULL<<32)
#define BITMAP_MASK (BITMAP_SIZE - 1)

Bitmap* init_bitmap(void)
{
    return init_bitmap_size(BITMAP_SIZE);
}

Bitmap* init_bitmap_size(uint64_t size)
{
	Bitmap* bitmap;
	uint64_t remainder;

	if (size == 0 || size > BITMAP_SIZE) {
		size = BITMAP_SIZE;
	} else {
		remainder = size % 8;
		if (remainder != 0) {
			size += 8 - remainder;
		}
	}

	bitmap = (Bitmap*)malloc(sizeof(Bitmap)+(size>>3));
	bitmap->bitsize = size - 1;
	bitmap->maxpos = 0;

	return bitmap;
}

bool set_bit(Bitmap* bitmap, uint64_t offset, uint8_t value)
{
	uint64_t index, pos;

	index = offset / 8;
	pos = offset % 8;

	if (bitmap->bitsize < offset) {
		return false;
	}

	if (value) {
		bitmap->data[index] |= 1 << pos;

		if (bitmap->maxpos < offset) {
			bitmap->maxpos = offset;
		}
	} else {
		bitmap->data[index] &= BITMAP_MASK ^ (1 << pos);
	}

	return true;
}

uint8_t get_bit(Bitmap* bitmap, uint64_t offset)
{
	uint64_t index, pos;

	index = offset / 8;
	pos = offset % 8;

	if (bitmap->bitsize < offset) {
		return false;
	}

	return (bitmap->data[index] >> pos) & 0x01;
}

void free_bitmap(Bitmap* bitmap)
{
	free(bitmap);
}
