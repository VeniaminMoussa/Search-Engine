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

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class LuceneConstants {
    
    public static final String INDEX_DIRECTORY = "/index";
    public static final String ID = "ID";
    public static List<String> CONSTANT = new LinkedList<String>(){{
        add("status_message");
        add("link_name");
        add("status_type");
        add("status_link");
        add("status_published");
        add("num_reactions");
        add("num_comments");
        add("num_shares");
        add("num_likes");
        add("num_loves");
        add("num_wows");
        add("num_hahas");
        add("num_sads");
        add("num_angrys");
    }};

    public static int MAX_SEARCH = 20;
}
