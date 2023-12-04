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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
/**
 *
 * @author Lenovo
 */
public class Searcher {
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;
    private QueryParser  queryParser;
    private Query query;

    public Searcher(String indexDirectoryPath) throws IOException {
       Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath).toPath());
       indexReader = DirectoryReader.open(indexDirectory);
       indexSearcher = new IndexSearcher(indexReader);
       Map<String,Analyzer> analyzerMap = new HashMap<>();
       analyzerMap.put("status_message", new EnglishAnalyzer());
       analyzerMap.put("link_name", new EnglishAnalyzer());
       analyzerMap.put("status_type", new SimpleAnalyzer());
       analyzerMap.put("status_link", new StandardAnalyzer());
       PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(new KeywordAnalyzer(), analyzerMap);
       queryParser = new QueryParser(LuceneConstants.CONSTANT.get(0),wrapper);
       queryParser.setAllowLeadingWildcard(true);
    }

    public TopDocs search( String searchQuery) throws IOException, ParseException {
       query = queryParser.parse(searchQuery);
       return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
       return indexSearcher.doc(scoreDoc.doc);
    }
  
    public int numDocs() throws CorruptIndexException, IOException {
       return indexReader.numDocs();
    }

    public void close() throws IOException {
        indexSearcher.getIndexReader().close();
    }
}
