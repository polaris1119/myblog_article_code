// Copyright 2014 The StudyGolang Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.
// http://studygolang.com
// Author：polaris	studygolang@gmail.com

package com.studygolang.bitmap;

public class Bitmap {

	// 暂时只支持 1<<32 位
	public final static long BITMAP_SIZE = 1L << 32;
	public final static long BITMAP_MASK = Bitmap.BITMAP_SIZE - 1;

	private byte[] data;

	private long bitsize;
	private long maxpos;

	public Bitmap() {
		this(Bitmap.BITMAP_SIZE);
	}

	public Bitmap(long size) {
		if (size == 0 || size > Bitmap.BITMAP_SIZE) {
			size = Bitmap.BITMAP_SIZE;
		} else {
			long remainder = size % 8;
			if (remainder != 0) {
				size += 8 - remainder;
			}
		}

		int sizeLen = (int) (size >> 3);
		this.data = new byte[sizeLen];

		this.bitsize = size;
		this.maxpos = 0;
	}

	public boolean setBit(long offset, int value) {
		if (this.bitsize < offset) {
			return false;
		}

		int index = (int) offset / 8;
		int pos = (int) offset % 8;

		if (value == 1) {
			this.data[index] |= 1 << pos;
			if (this.maxpos < offset) {
				this.maxpos = offset;
			}
		} else {
			this.data[index] &= Bitmap.BITMAP_MASK ^ (1 << pos);
		}

		return true;
	}

	public int getBit(long offset) {
		if (this.bitsize < offset) {
			return 0;
		}

		int index = (int) (offset / 8);
		int pos = (int) (offset % 8);

		return (this.data[index] >> pos) & 0x01;
	}

	public long getMaxpos() {
		return this.maxpos;
	}
}