package com.definesys.dsgc.service.ystar.utils;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MavenUtils
 * @Description: maven操作工具类
 * @Author：afan
 * @Date : 2020/1/2 14:32
 */
public class MavenUtils {
    public static boolean mvnCleanPackage(String projectPath, String mavenHome) {
        System.out.println("projectPath->"+projectPath+" mavenHome->"+mavenHome);
        try {
            InvocationRequest request = new DefaultInvocationRequest();
            //request.setProperties(properties);
            Invoker invoker = new DefaultInvoker();
            List<String> mavenGoals = new ArrayList<>();
            mavenGoals.add("clean");
            mavenGoals.add("package");
            request.setGoals(mavenGoals);
            InvocationResult invoke = invoke(request, projectPath, mavenHome);
            int exitCode = invoke.getExitCode();
            if (exitCode == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static InvocationResult invoke(final InvocationRequest request, final String path, String mavenHome) {
        InvocationResult result = null;
        final Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(mavenHome));
        try {
            invoker.setWorkingDirectory(new File(path));
            result = invoker.execute(request);
        } catch (MavenInvocationException ex) {
            final String messageError = "Maven exception: " + ex.getMessage();

        }
        return result;
    }

    public static void main(String[] args) {

    }
}
