/*
 * Βενιαμίν Μούσσα
 * Α.Μ: 2022201700115
 * username: dit17115
 * email: dit17115@uop.gr
 *
 * https://sourceforge.net/projects/opencsv/files/latest/download
 *
 *https://commons.apache.org/proper/commons-lang/download_lang.cgi
 *
 *https://sourceforge.net/projects/jdatepicker/files/latest/download
 */
package trashluceneproject;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Lenovo
 */
public class CSVFileFilter implements FileFilter{

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".csv");
    }
    
}
