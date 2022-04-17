import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.wjl.fwwb.common.constants.GlobalConstants;
import com.wjl.fwwb.service.intf.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

/**
 * @author: wjl
 * @time: 2022/4/12 19:45
 * @version: 1.0
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private COSClient createClient() {
        var secretId = GlobalConstants.SECRET_ID;
        var secretKey = GlobalConstants.SECRET_KEY;
        var credentials = new BasicCOSCredentials(secretId, secretKey);
        var region = new Region(GlobalConstants.REGION);
        var clientConfig = new ClientConfig(region);
        return new COSClient(credentials, clientConfig);
    }

    @Override
    public String upload(MultipartFile file) {
        var client = createClient();
        try {
            var tmp = File.createTempFile("tmp", null);
            file.transferTo(tmp);
            var bucket = GlobalConstants.BUCKET;
            var prefix = GlobalConstants.PREFIX;
            var filename = file.getOriginalFilename();
            var putRequest = new PutObjectRequest(bucket, prefix + filename, tmp);
            var putResult = client.putObject(putRequest);
            var region = GlobalConstants.REGION;
            log.info(putResult.getETag());
            return "https://" + bucket + ".cos." + region + ".myqcloud.com/" + prefix + filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            client.shutdown();
        }
    }

    @Override
    public String upload(File file) {
        var client = createClient();
        try {
            var bucket = GlobalConstants.BUCKET;
            var prefix = GlobalConstants.PREFIX;
            var putRequest = new PutObjectRequest(bucket, prefix + file.getName(), file);
            var putResult = client.putObject(putRequest);
            var region = GlobalConstants.REGION;
            log.info(putResult.getETag());
            return "https://" + bucket + ".cos." + region + ".myqcloud.com/" + prefix + file.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            client.shutdown();
        }
    }

    @Override
    public String upload(File file, String filename) {
        var client = createClient();
        try {
            var bucket = GlobalConstants.BUCKET;
            var prefix = GlobalConstants.PREFIX;
            var putRequest = new PutObjectRequest(bucket, prefix + filename, file);
            var putResult = client.putObject(putRequest);
            var region = GlobalConstants.REGION;
            log.info("upload success " + putResult.getETag());
            return "https://" + bucket + ".cos." + region + ".myqcloud.com/" + prefix + filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            client.shutdown();
        }
    }

}
