package br.unb.cic.bionimbuz.compactor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Vinicius
 */
public class GZipCompactor implements Compactor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GZipCompactor.class);

    @Override
    public String compact(String inputFile) {
        File out = new File(inputFile + ".gzip");

        try {
            GZIPOutputStream gzip = new GZIPOutputStream(new FileOutputStream(out));
            gzip.write(IOUtils.toByteArray(new FileReader(inputFile)));
            IOUtils.closeQuietly(gzip);

        } catch (FileNotFoundException ex) {
            LOGGER.error("[FileNotFoundException] - " + ex.getMessage());
        } catch (IOException ex) {
            LOGGER.error("[IOException] - " + ex.getMessage());
        }

        return out.getAbsolutePath();
    }

}
