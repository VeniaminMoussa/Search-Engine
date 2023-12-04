/*
 * Βενιαμίν Μούσσα
 * Α.Μ: 2022201700115
 * username: dit17115
 * email: dit17115@uop.gr
 *
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trashluceneproject;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Lenovo
 */
public class Indexer {
    private IndexWriter writer;
    private IndexReader indexReader;

    public Indexer(String indexDirectoryPath) throws IOException{
      //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
        
      //create the indexer
        Map<String,Analyzer> analyzerMap = new HashMap<>();
        analyzerMap.put("status_message", new EnglishAnalyzer());
        analyzerMap.put("link_name", new EnglishAnalyzer());
        analyzerMap.put("status_type", new SimpleAnalyzer());
        analyzerMap.put("status_link", new StandardAnalyzer());
        PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(new KeywordAnalyzer(), analyzerMap);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(wrapper);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        
        writer = new IndexWriter(indexDirectory, indexWriterConfig);
        writer.commit();    
        indexReader = DirectoryReader.open(indexDirectory);
    }

    public int createIndex(File[] files, CSVFileFilter filter) 
        throws IOException {
        //get all files in the data directory

        for (File file : files){
           if(!file.isDirectory()
              && !file.isHidden()
              && file.exists()
              && file.canRead()
              && filter.accept(file)
           ){
              this.CSVExtractionIndexer(file);
           }
        }
        return writer.numRamDocs();
    }
    
    private void CSVExtractionIndexer(File file) throws IOException{
        System.out.println("Indexing "+file.getCanonicalPath());
        try {
            // create csvReader object and read first Line Headers
            CSVReader csvReaderHeader = new CSVReader(new FileReader(file)); 
            String[] headersRecord;
            
            LuceneConstants.CONSTANT.clear();
            
            if((headersRecord = csvReaderHeader.readNext()) != null) {
                for (String header : headersRecord){
                    LuceneConstants.CONSTANT.add(header);
                }
            }
            // create csvReader object and skip first Line 
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();
            int lastId = 0;
            int id =lastId;
            if(indexReader.numDocs() != 0){
                for(int i=0;i<indexReader.numDocs();i++){
                    if(lastId< Integer.parseInt(indexReader.document(i).get(LuceneConstants.ID))){
                        lastId = Integer.parseInt(indexReader.document(i).get(LuceneConstants.ID));
                    }
                }
                id = lastId+1;
            }

            this.indexReader.close();
            for (String[] row : allData) {
                Document document = new Document();
                
                for(int i=0;i<row.length;i++){
                    //index file parameters
                    if(i == 0){
                        document.add(new TextField(LuceneConstants.ID, String.valueOf(id),Field.Store.YES));
                    }
                    if(LuceneConstants.CONSTANT.get(i).contains("num")){
                        if(row[i].isEmpty()){
                            document.add(new TextField(LuceneConstants.CONSTANT.get(i),"0",Field.Store.YES));
                        }
                        else{
                            try{
                                Integer.parseInt(row[i]);
                                document.add(new TextField(LuceneConstants.CONSTANT.get(i), row[i],Field.Store.YES));
                            }catch(NumberFormatException e){
                                break;
                            }
                        }
                    }
                    else if(LuceneConstants.CONSTANT.get(i).contains("published")){
                        if(row[i].isEmpty()){
                            document.add(new TextField(LuceneConstants.CONSTANT.get(i),String.valueOf(-1),Field.Store.YES));
                        }
                        else{
                            try{
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm");
                                Date parsedDate = dateFormat.parse(row[i]);

                                Long dateTime = DateTools.stringToTime(DateTools.dateToString(parsedDate, DateTools.Resolution.MILLISECOND));

                                document.add(new TextField(LuceneConstants.CONSTANT.get(i),String.valueOf(dateTime),Field.Store.YES));
                            }catch(ParseException ex){
                                break;
                            }
                        }
                    }
                    else{
                        
                        document.add(new TextField(LuceneConstants.CONSTANT.get(i), row[i],Field.Store.YES));
                    }
                }
                if(document.getFields().size() == LuceneConstants.CONSTANT.size()+1){
                    writer.addDocument(document);
                    id++;
                }
            }
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        } 
    }
    
    public void updateDoc(Post post) throws CorruptIndexException, IOException {
        
       this.deleteDoc(post.getPostField(LuceneConstants.ID));
       
       Document document = new Document();
                
       document.add(new TextField(LuceneConstants.ID, String.valueOf(post.getPostField(LuceneConstants.ID)),Field.Store.YES));
       
       for(int i=0;i < LuceneConstants.CONSTANT.size();i++){
           document.add(new TextField(LuceneConstants.CONSTANT.get(i), post.getPostField(LuceneConstants.CONSTANT.get(i)),Field.Store.YES));
       }
       
       writer.addDocument(document);
    }
    
    public void deleteDoc(String id) throws CorruptIndexException, IOException {
       writer.deleteDocuments(new Term(LuceneConstants.ID,String.valueOf(id)));
    }
    
    public void clear() throws CorruptIndexException, IOException {
       writer.deleteAll();
    }

    public void close() throws CorruptIndexException, IOException {
       writer.close();
    }
}


