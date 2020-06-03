package com.xrlj.codegen.processor;

import com.xrlj.utils.StringUtil;

import java.io.File;

public final class GenCodeUtils {

    private GenCodeUtils(){}

    /**
     * java file要输出模块的src/main/java路径
     * @param gencodeProjectName
     * @param javaFileOputProjectName
     * @return
     */
    public static String fileOutputSrcMailJava(String gencodeProjectName, String javaFileOputProjectName) {
        String javaOutDir;
        File userDirFile = new File(System.getProperty("user.dir"));
        String path = userDirFile.getPath();
        int i = path.lastIndexOf(File.separator);
        String str = path.substring(i + 1);
        if (gencodeProjectName.equals(str)) {
            javaOutDir = userDirFile.getParent();
        } else {
            javaOutDir = path;
        }
        String javaOutDirLaster = javaOutDir.concat(File.separator).concat(javaFileOputProjectName).concat(File.separator).concat("src").concat(File.separator).concat("main").concat(File.separator).concat("java");

        return javaOutDirLaster;
    }

    /**
     * 判断某个java类文件是否已经存在
     * @param javaFileOutRootDir src/main/java目录路径
     * @param packagePath 需要输出到模块下的包名
     * @param javaFileName 文件名
     * @return true存在，否则不存在
     */
    public static boolean containsJavaFile(String javaFileOutRootDir, String packagePath, String javaFileName) {
        if (StringUtil.isEmpty(javaFileOutRootDir)) {
            return false;
        }
        if (StringUtil.isEmpty(packagePath)) {
            return false;
        }
        if (!packagePath.contains(".")) {
            return false;
        }

        StringBuilder sb = new StringBuilder(javaFileOutRootDir);
        String[] splits = packagePath.split("\\.");
        for (int i = 0; i < splits.length; i++) {
            sb.append(File.separator).append(splits[i]);
        }
        File file = new File(sb.append(File.separator).append(javaFileName.concat(".java")).toString());
        System.out.println(file.getPath());
        if (file.exists()) {
            return true; //存在返回true
        }
        return false;
    }
}
