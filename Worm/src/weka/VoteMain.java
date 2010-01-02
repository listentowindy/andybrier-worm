package weka;

import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SelectedTag;

public class VoteMain {

	private String fileInstance = "weka/total.arff";
	private Instances data = null; // contains the full dataset

	// 分类器集合
	private Classifier[] classfilers = { new J48(), new Id3(),
			new NaiveBayes(), new LibSVM(), new SMO() };

	public static void main(String[] args) {
		new VoteMain().run();
	}

	public void run() {

		// 读取file到instance
		FileReader frData;
		try {
			frData = new FileReader(fileInstance);
			data = new Instances(frData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Class Index是指示用于分类的目标属性:一般是最后一个属性
		data.setClassIndex(data.numAttributes() - 1);

		// Vote
		Vote vote = new Vote();
		SelectedTag tag1 = new SelectedTag(Vote.MAJORITY_VOTING_RULE,
				Vote.TAGS_RULES);

		vote.setClassifiers(classfilers); // 设置分类器集合
		vote.setCombinationRule(tag1); // 设置vote标准
		vote.setSeed(2); // 设置种子
		
		//10则交叉验证，得到结果
		Evaluation eval = null;
		try {
			eval = new Evaluation(data);
			eval.crossValidateModel(vote, data, 10, new Random(1));

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 输出结果
		System.out.println(" **************Vote********** ");
		System.out.println("Accuracy: " + (1 - eval.errorRate()));

	}
}
