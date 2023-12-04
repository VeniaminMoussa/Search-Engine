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
import org.apache.commons.lang3.tuple.Pair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lenovo
 */
public class Post {
    private List<Pair<String, String>> postField;
    
    public Post() {
        this.postField = new LinkedList<>();
    }
    
    public String getPostField(String constant) {
        
        for(int i=0; i<this.postField.size();i++){
            if(this.postField.get(i).getKey().compareToIgnoreCase(constant)==0){
                return this.postField.get(i).getValue();
            }
        }
        return null;
    }
    
    public void addPostField(String constant, String field) {
        this.postField.add(Pair.of(constant, field));
    }
    
    public void setPostField(String constant, String field) {
        for(int i=0;i<this.postField.size();i++){
            if(this.postField.get(i).getKey().compareToIgnoreCase(constant)==0){
                this.postField.remove(i);
                this.postField.add(i, Pair.of(constant, field));
            }
        }
    }
}
