import java.util.HashMap;
import java.util.Set;

/**
 * This class implements the Vector-Space model.
 * It takes a corpus and creates the tf-idf vectors for each document.
 * @author swapneel
 *
 */
public class VectorSpaceModel {
	
	/**
	 * The corpus of documents.
	 */
	private Corpus corpus;
	
	/**
	 * The tf-idf weight vectors.
	 * The hashmap maps a document to another hashmap.
	 * The second hashmap maps a term to its tf-idf weight for this document.
	 */
	private HashMap<Document, HashMap<String, Double>> tfIdfWeights;

	public HashMap<Document, HashMap<String, Double>> getTfIdfWeights() {
		return tfIdfWeights;
	}

	/**
	 * The constructor.
	 * It will take a corpus of documents.
	 * Using the corpus, it will generate tf-idf vectors for each document.
	 * @param corpus the corpus of documents
	 */
	public VectorSpaceModel(Corpus corpus) {
		this.corpus = corpus;
		tfIdfWeights = new HashMap<Document, HashMap<String, Double>>();
		
		createTfIdfWeights();
	}

	/**
	 * This creates the tf-idf vectors.
	 */
	public void createTfIdfWeights() {
		System.out.println("Creating the tf-idf weight vectors");
		Set<String> terms = corpus.getInvertedIndex().keySet();
		
		for (Document document : corpus.getDocuments()) {
			HashMap<String, Double> weights = new HashMap<String, Double>();
			
			for (String term : terms) {
				double tf = document.getTermFrequency(term);
				double idf = corpus.getInverseDocumentFrequency(term);
				
				double weight = tf * idf;
				weights.put(term, weight);
			}
			tfIdfWeights.put(document, weights);
		}
	}
	
	/**
	 * This method will return the magnitude of a vector.
	 * @param document the document whose magnitude is calculated.
	 * @return the magnitude
	 */
	public double getMagnitude(Document document) {
		double magnitude = 0;
		HashMap<String, Double> weights = tfIdfWeights.get(document);
		
		for (double weight : weights.values()) {
			magnitude += weight * weight;
		}
		
		return Math.sqrt(magnitude);
	}

	/**
	 * This method will return the magnitude of a vector (without a document).
	 * @param v the vector whose magnitude is calculated.
	 * @return the magnitude
	 */
	public static double getMagnitude(HashMap<String, Double> v) {
		double magnitude = 0;

		for (double weight : v.values()) {
			magnitude += weight * weight;
		}

		return Math.sqrt(magnitude);
	}

	/**
	 * This will take two documents and return the dot product.
	 * @param d1 Document 1
	 * @param d2 Document 2
	 * @return the dot product of the documents
	 */
	public double getDotProduct(Document d1, Document d2) {
		double product = 0;
		HashMap<String, Double> weights1 = tfIdfWeights.get(d1);
		HashMap<String, Double> weights2 = tfIdfWeights.get(d2);
		
		for (String term : weights1.keySet()) {
			product += weights1.get(term) * weights2.get(term);
		}
		
		return product;
	}

	/**
	 * This will take the dot product between two weight vectors
	 * @param v1 Vector 1
	 * @param v2 Vector 2
	 * @return the dot product of the vectors
	 */
	public static double getDotProduct(HashMap<String, Double> v1, HashMap<String, Double> v2) {
		double product = 0;
		for (String term : v1.keySet()) {
			product += v1.get(term) * v2.get(term);
		}

		return product;
	}
	
	/**
	 * This will return the cosine similarity of two documents.
	 * This will range from 0 (not similar) to 1 (very similar).
	 * @param d1 Document 1
	 * @param d2 Document 2
	 * @return the cosine similarity
	 */
	public double cosineSimilarity(Document d1, Document d2) {
		return getDotProduct(d1, d2) / (getMagnitude(d1) * getMagnitude(d2));
	}

	/**
	 * This will return the cosine similarity of two vectors.
	 * This will range from 0 (not similar) to 1 (very similar).
	 * @param v1 Vector 1
	 * @param v2 Vector 2
	 * @return the cosine similarity of the vectors
	 */
	public static double cosineSimilarity(HashMap<String, Double> v1, HashMap<String, Double> v2) {
		return getDotProduct(v1, v2) / (getMagnitude(v1) * getMagnitude(v2));
	}
}