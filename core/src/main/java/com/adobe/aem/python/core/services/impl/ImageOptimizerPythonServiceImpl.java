package com.adobe.aem.python.core.services.impl;

import com.adobe.aem.python.core.config.ImageOptimizerConfiguration;
import com.adobe.aem.python.core.services.ImageOptimizerPythonService;
import org.apache.commons.io.FileUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
/**
 * ServiceImpl for sending payloadPath from workflow to service and then to Python script
 * getting the python script stored in src/main/resourcces
 * @author sruthi.adabettu@embitel.com
 *
 */
@Component(service = { ImageOptimizerPythonService.class }, immediate = true)
@Designate(ocd = ImageOptimizerConfiguration.class)
public class ImageOptimizerPythonServiceImpl implements ImageOptimizerPythonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageOptimizerPythonServiceImpl.class);
    private String instanceUrl;
    private String userName;
    private String password;

    @Activate
    public void activate(final ImageOptimizerConfiguration config) {
        instanceUrl = config.instanceurl();
        userName = config.username();
        password = config.password();
    }

    /**
     * Pass the payloadPath from workflow accept the inputStream and convert that into a file
     * read the file and convert to String with arguments
     *
     * @param payloadPath
     *            String
     * @return String with python script with arguments
     * @throws IOException
     */
    @Override
    public String getPythonScriptMessage(String payloadPath) throws IOException {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("image_optimizer.py");
        File file=new File("image_optimizer.py");

        FileUtils.copyInputStreamToFile(in, file);
        modifyWordInFile(file, "image_path_from_java", payloadPath );
        modifyWordInFile(file,"instanceurl", instanceUrl);
        modifyWordInFile(file,"username", userName);
        modifyWordInFile(file,"password", password);
        ProcessBuilder processBuilder = new ProcessBuilder("python");
        processBuilder.redirectInput(file);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        List<String> results = readProcessOutput(process.getInputStream());

        return results.toString();
    }

    /**
     * Pass the inputStream to using Buffered Reader converts into list of Strings
     *
     * @param inputStream
     *            InputStream
     * @return Read the inputStream convert that to list of Strings
     * @throws IOException
     */
    private List<String> readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }

    /**
     * Pass the file, oldWord and newWord as a parameters and replace the words in the python
     * script and these params can be treated as arguments for the python script.
     *
     * @param file
     *            File
     * @param oldWord
     *            String
     * @param newWord
     *            String
     * @return the oldWord , Strings are  replaced with newWord when the python file is created
     * @throws IOException
     */
    public static void modifyWordInFile(File file, String oldWord, String newWord) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            bufferedReader.close();
            String modifiedContent = content.toString().replace(oldWord, newWord);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(modifiedContent);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

