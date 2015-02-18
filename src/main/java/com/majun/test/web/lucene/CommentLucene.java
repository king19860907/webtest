package com.majun.test.web.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.util.CollectionUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.majun.test.web.lucene.dao.CommentDao;
import com.majun.test.web.lucene.dto.CommentDto;

public class CommentLucene {
	
	/**
	 * 指定索引存放目录
	 * @param args
	 */
	private final String dirPath="D:\\luence";
	
	public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
		CommentLucene lucene = new CommentLucene();
		
		//使用一元分词
		//lucene.initComments(new StandardAnalyzer(Version.LUCENE_35));
		//lucene.search("上海", 1000, new StandardAnalyzer(Version.LUCENE_35));
		
		//使用二元分词
		//lucene.initComments(new CJKAnalyzer(Version.LUCENE_35));
		//lucene.search("上海", 1000, new CJKAnalyzer(Version.LUCENE_35));
		
		//IK词库分词
		lucene.initComments(new IKAnalyzer());
		lucene.search("上海", 1000, new IKAnalyzer());
	}
	
	public List<Document> search(String searchContent,int size,Analyzer analyzer) throws IOException, ParseException, InvalidTokenOffsetsException {
		
		Directory diretory = FSDirectory.open(new File(dirPath));
		IndexReader reader = IndexReader.open(diretory);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", analyzer);
		Query query = parser.parse(searchContent);
		
		TopDocs tds = searcher.search(query, size);
		ScoreDoc[] scoreDocs = tds.scoreDocs;
		List<Document> docs = new ArrayList<Document>();
		
		//高亮显示
		QueryScorer scorer=new QueryScorer(query);  
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);  
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"hightlighterCss\">","</span>");  
		Highlighter highlight=new Highlighter(formatter,scorer);  
		highlight.setTextFragmenter(fragmenter);  
		
		if(scoreDocs != null && scoreDocs.length > 0) {
			for(ScoreDoc scoreDoc : scoreDocs) {
				Document doc = searcher.doc(scoreDoc.doc);
				if(doc != null) {
					TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(doc.get("content")));
					String content = highlight.getBestFragment(tokenStream, doc.get("content"));    
					System.out.println(doc.get("userName")+":"+content);
				}
				docs.add(doc);
			}
			searcher.close();
			return docs;
		}
		searcher.close();
		return null;
	}

	public void initComments(Analyzer analyzer) throws IOException {
		
		CommentDao commentDao = new CommentDao();
		List<CommentDto> comments = commentDao.findComments();
		
		Directory directory = FSDirectory.open(new File(dirPath));
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
		IndexWriter writer = new IndexWriter(directory,config);
		
		if(!CollectionUtils.isEmpty(comments)) {
			for(CommentDto dto : comments) {
				Document doc = new Document();
				doc.add(new Field("id", String.valueOf(dto.getId()),Field.Store.YES,Index.NOT_ANALYZED));
				doc.add(new Field("userId", String.valueOf(dto.getUserId()),Field.Store.YES,Index.NOT_ANALYZED));
				doc.add(new Field("userName", String.valueOf(dto.getUserName()),Field.Store.YES,Index.ANALYZED));
				doc.add(new Field("content", String.valueOf(dto.getContent()),Field.Store.YES,Index.ANALYZED));
				writer.addDocument(doc);
			}
		}
		
		writer.close();
	}
	
}
