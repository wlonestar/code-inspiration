import com.wjl.fwwb.common.constants.PredictReult;
import com.wjl.fwwb.common.enums.PredictEnum;
import com.wjl.fwwb.service.intf.PredictService;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;

/**
 * @author: wjl
 * @time: 2022/4/11 21:59
 * @version: 1.0
 */
@Service
public class PredictServiceImpl implements PredictService {

    @Override
    public PredictReult predict(String imagePath) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        String homeDirectory = System.getProperty("user.dir");
        String pythonFile = "predict.py";
        Process process;
        try {
            String scriptName;
            if (isWindows) {
                scriptName = "shell.ps1";
                process = Runtime.getRuntime().exec(
                        String.format("powershell.exe %s -name %s -path %s", scriptName, pythonFile, imagePath),
                        null, new File(homeDirectory));
            } else {
                scriptName = "shell.bash";
                process = Runtime.getRuntime().exec(
                        String.format("/bin/bash %s -n %s -p %s", scriptName, pythonFile, imagePath),
                        null, new File(homeDirectory));
            }
            var reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            var res = "";
            if ((res = reader.readLine()) != null) {
                String[] sp = res.split(" ");
                var prob = Double.valueOf(sp[0]);
                var index = Integer.parseInt(sp[2]);
                PredictEnum predictEnum = new PredictEnum();
                return predictEnum.getResult(prob, index);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
