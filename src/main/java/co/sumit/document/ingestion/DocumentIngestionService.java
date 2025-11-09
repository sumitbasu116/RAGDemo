package co.sumit.document.ingestion;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DocumentIngestionService implements CommandLineRunner{

	@Value("classpath:/pdf/spring-boot-reference.pdf")
	private Resource resource;
	
	private final VectorStore vectorStore;
	
	public DocumentIngestionService(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}
	
	@Override
	public void run(String... args) throws Exception {
		// read the pdf
		TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
		//split the pdf contents into chunks
		TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
		List<Document> documents = tokenTextSplitter.split(tikaDocumentReader.read());
		//save the data in the vector database
		vectorStore.accept(documents);
	}
}
