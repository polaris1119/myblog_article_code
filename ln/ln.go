package main

import (
	"fmt"
	"os"
	"path/filepath"
)

func main() {

	if len(os.Args) != 3 {
		fmt.Println("ln target linkname")
		os.Exit(1)
	}

	curDir, _ := os.Getwd()

	target := os.Args[1]
	if !filepath.IsAbs(target) {
		target = curDir + string(os.PathSeparator) + target
	}

	linkname := os.Args[2]
	if !filepath.IsAbs(linkname) {
		linkname = curDir + string(os.PathSeparator) + linkname
	}

	err := os.Symlink(target, linkname)

	if err != nil {
		fmt.Printf("创建失败：%s\n", err)
		os.Exit(2)
	}
}
