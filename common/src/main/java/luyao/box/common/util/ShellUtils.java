package luyao.box.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by luyao
 * on 2018/11/1 13:56
 */
public class ShellUtils {

    public static CommandResult execCommand(List<String> command, boolean isRoot) {
        return execCommand((String[]) command.toArray(), isRoot);
    }

    public static CommandResult execCommand(String command, boolean isRoot) {
        return execCommand(new String[]{command}, isRoot);
    }

    public static CommandResult execCommand(String[] commands, boolean isRoot) {

        int result = -1;
        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();

        Process process = null;
        DataOutputStream os = null;
        BufferedReader resultReader = null;
        BufferedReader errorReader = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                os.write(command.getBytes());
                os.writeBytes("\n");
                os.flush();
            }
            os.writeBytes("exit\n");
            os.flush();

            String line;
            result = process.waitFor();

            resultReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")), 4096);
            while ((line = resultReader.readLine()) != null) {
                resultBuilder.append(line);
            }

            errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), Charset.forName("UTF-8")), 4096);
            while ((line = errorReader.readLine()) != null) {
                errorBuilder.append(line);
            }
        } catch (IOException e) {
            errorBuilder.append("\n")
                    .append(e.getMessage());
        } catch (InterruptedException e) {
            errorBuilder.append("\n")
                    .append(e.getMessage());
        } finally {
            CloseUtils.close(os, resultReader, errorReader);
            if (process != null) process.destroy();
        }
        return new CommandResult(resultBuilder.toString(), errorBuilder.toString(), result);
    }

    public static class CommandResult {
        String successResult;
        String errorResult;
        int result; // 0  normal termination

        CommandResult(String successResult, String errorResult, int result) {
            this.successResult = successResult;
            this.errorResult = errorResult;
            this.result = result;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                    "successResult='" + successResult + '\'' +
                    ", errorResult='" + errorResult + '\'' +
                    ", result=" + result +
                    '}';
        }
    }
}
