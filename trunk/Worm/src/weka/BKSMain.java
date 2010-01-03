package weka;

import weka.core.Instances;

/**
 * 对total.arff就行10次BKS,计算出平均的准确率
 * @author andy
 *
 */
public class BKSMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BKS bks = new BKS();

		Instances train = null;
		Instances test = null;
		Instances all = null;
		Instances trainTest[] = null;

		String file = "weka/total.arff"; // 要就行试验的arff文件

		double accury[] = new double[10]; // 10次准确率

		/**
		 * 1.对total.arff就行10次划分，分别对每次划分计算BKS准确率
		 */
		int n = 10;
		for (int i = 0; i < n; i++) {

			// 分割训练和测试。训练有70%
			all = WekaUtil.getInstances(file);
			trainTest = WekaUtil.filterData(all, 70);
			train = trainTest[0];
			test = trainTest[1];

			// 设置classIndex
			train.setClassIndex(train.numAttributes() - 1);
			test.setClassIndex(test.numAttributes() - 1);

			// BKS
			try {
				
				bks.train(train);
				accury[i] = bks.test(train, test);
				
				System.out.println("已经完成了第"+(i+1)+"次");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			

		}
		
		//输出每次结果和平均值
		double amount = 0;
		
		for(double d : accury){
			amount = amount+d;
		   System.out.print("  "+d);
		}
		
		double avg = amount/10;
		 System.out.print("\n");
		 System.out.print("10 次BKS的平均值是  " + avg);

	}

}
