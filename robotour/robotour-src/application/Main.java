package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 15.3.12
 * Time: 21:21
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    
    public static void main(String[] args) throws IOException {
        Process process = Runtime.getRuntime().exec("C:\\Users\\Kotuc\\Projects\\Visual Studio\\HelloOpenCV\\HelloOpenCV\\Debug\\HelloOpenCV.exe");
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = bufferedReader.readLine())!=null) {
            System.out.println("Vis: "+line);
        }
        System.exit(0);
    }

}
