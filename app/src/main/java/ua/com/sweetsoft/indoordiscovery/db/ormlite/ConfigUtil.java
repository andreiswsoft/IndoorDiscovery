package ua.com.sweetsoft.indoordiscovery.db.ormlite;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class ConfigUtil extends OrmLiteConfigUtil
{
    private static final String CONFIG_FILE_NAME = "ormlite_config.txt";
    private static final Class<?>[] classes = new Class[]{Network.class, SignalSample.class};

    public static void main(String[] args) throws SQLException, IOException
    {
        File file = getConfigFile();
        writeConfigFile(file, classes);
    }

    private static File getConfigFile()
    {
        File file = new File("");
        if (file != null)
        {
            file = new File(file.getAbsolutePath());
            if (file != null)
            {
                file = findConfigFile(file);
            }
        }
        return file;
    }

    protected static File findConfigFile(File dir)
    {
        File configFile = null;
        for (File file : dir.listFiles())
        {
            if (file.isDirectory())
            {
                file = findConfigFile(file);
            }
            if (file == null || !file.getName().equals(CONFIG_FILE_NAME))
            {
                continue;
            }
            String path = file.getAbsolutePath();
            if (!path.endsWith(RESOURCE_DIR_NAME + File.separator + RAW_DIR_NAME + File.separator + CONFIG_FILE_NAME))
            {
                continue;
            }
            configFile = file;
            break;
        }
        return configFile;
    }

}
