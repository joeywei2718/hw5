import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * the tester class.
 * @author swapneel
 */
public class VectorSpaceModelTester {

    public static void main(String[] args) {
        String directory = "C:/Users/benja/Downloads/ir/ir";
        //Document query = new Document(directory + "/query.txt");
        Document nyc1 = new Document(directory + "/nyc-1.txt");
        Document nyc2 = new Document(directory + "/nyc-2.txt");
        Document nyc3 = new Document(directory + "/nyc-3.txt");
        Document nyc1bard = new Document(directory + "/nyc-bard-1.txt");
        Document nyc2bard = new Document(directory + "/nyc-bard-2.txt");
        Document nyc3bard = new Document(directory + "/nyc-bard-3.txt");
        Document penn1 = new Document(directory + "/penn-1.txt");
        Document penn2 = new Document(directory + "/penn-2.txt");
        Document penn3 = new Document(directory + "/penn-3.txt");
        Document penn1bard = new Document(directory + "/penn-bard-1.txt");
        Document penn2bard = new Document(directory + "/penn-bard-2.txt");
        Document penn3bard = new Document(directory + "/penn-bard-3.txt");
        //Document d1 = new Document("alice-in-wonderland.txt");
        //Document d2 = new Document("huck-finn.txt");
        //Document d3 = new Document("les-mis.txt");
        //Document d4 = new Document("tom-sawyer.txt");
        //Document d5 = new Document("pride-prejudice.txt");

        ArrayList<Document> documents = new ArrayList<Document>();
        documents.add(nyc1);
        documents.add(nyc2);
        documents.add(nyc3);
        documents.add(penn1);
        documents.add(penn2);
        documents.add(penn3);
        documents.add(nyc1bard);
        documents.add(nyc2bard);
        documents.add(nyc3bard);
        documents.add(penn1bard);
        documents.add(penn2bard);
        documents.add(penn3bard);
        //documents.add(d1);
		/*documents.add(d2);
		documents.add(d3);
		documents.add(d4);
		documents.add(d5);*/

        Corpus corpus = new Corpus(documents);

        VectorSpaceModel vectorSpace = new VectorSpaceModel(corpus);

        for (int i = 0; i < documents.size(); i++) {
            for (int j = i + 1; j < documents.size(); j++) {
                Document doc1 = documents.get(i);
                Document doc2 = documents.get(j);
                System.out.println("\nComparing " + doc1 + " and " + doc2);
                System.out.println(vectorSpace.cosineSimilarity(doc1, doc2));
            }
        }

		/*for(int i = 1; i < documents.size(); i++) {
			Document doc = documents.get(i);
			System.out.println("\nComparing to " + doc);
			System.out.println(vectorSpace.cosineSimilarity(query, doc));
		}*/
    }

}
