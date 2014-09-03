// Copyright 2014 The StudyGolang Authors. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.
// http://studygolang.com
// Author：polaris	studygolang@gmail.com

// 使用 Bitmap 实现排重
// Usage taskdiff destfile onefile twofile ...
package main

import (
	"bufio"
	. "github.com/polaris1119/bitmap"
	"io"
	"log"
	"os"
	"runtime"
	"strconv"
	"strings"
	"sync"
	"time"
)

func main() {
	runtime.GOMAXPROCS(runtime.NumCPU())

	TaskDiff(os.Args[1], strings.Join(os.Args[2:], ","))

}

func TaskDiff(taskId, excTaskIds string) (err error) {
	start := time.Now()

	curToidFile := taskId

	bitmap := NewBitmap()

	writeToid2Bitmap(bitmap, curToidFile, 1)

	curSetTime := time.Now()
	log.Println("current task", taskId, "SetBit spend time:", curSetTime.Sub(start))

	var wg sync.WaitGroup

	excludes := strings.Split(excTaskIds, ",")

	for _, diffTask := range excludes {
		wg.Add(1)
		go func(taskId string) {
			defer wg.Done()
			writeToid2Bitmap(bitmap, taskId, 0)
		}(diffTask)
	}

	wg.Wait()

	log.Println("current task", taskId, "excludes[", excTaskIds, "]spend time:", time.Now().Sub(curSetTime))

	file, err := os.Create(taskId + "_diff")
	if err != nil {
		return err
	}
	defer file.Close()

	writer := bufio.NewWriter(file)

	var bitTotal = bitmap.Maxpos() + 1

	for offset := uint64(0); offset < bitTotal; offset++ {
		if bitmap.GetBit(offset) == 1 {
			writer.WriteString(strconv.FormatUint(offset, 10) + "\n")
		}
	}

	writer.Flush()

	log.Println("current task", taskId, "total spend time:", time.Now().Sub(start))

	return
}

func writeToid2Bitmap(bitmap *Bitmap, toidFile string, value uint8) error {
	file, err := os.Open(toidFile)
	if err != nil {
		return err
	}

	defer file.Close()

	reader := bufio.NewReader(file)
	line, err := reader.ReadString('\n')

	for ; err != io.EOF; line, err = reader.ReadString('\n') {
		toid, err := strconv.Atoi(strings.TrimSpace(line))
		if err != nil {
			log.Println("strconv Atoi error", err)
			continue
		}
		bitmap.SetBit(uint64(toid), value)
	}

	return nil
}
