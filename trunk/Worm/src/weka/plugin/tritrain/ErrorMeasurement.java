package weka.plugin.tritrain;

/**
 * Description: Use tri-training to exploit unlabeled data.
 *
 * Reference:   Z.-H. Zhou, Y. Jiang, M. Li. Tri-Training: exploiting unlabeled data using three
 *              classifiers. IEEE Transactions on Knowledge and Data Engineering, 2005, vol.17,
 *              no.11, pp.1529-1541.
 *
 * ATTN:        This package is free for academic usage. You can run it at your own risk.
 *	     	      For other purposes, please contact Prof. Zhi-Hua Zhou (zhouzh@nju.edu.cn).
 *
 * Requirement: To use this package, the whole WEKA environment (ver 3.4) must be available.
 *	            refer: I.H. Witten and E. Frank. Data Mining: Practical Machine Learning
 *		          Tools and Techniques with Java Implementations. Morgan Kaufmann,
 *		          San Francisco, CA, 2000. 
 *              A plug-in interface ErrorMeasurement is needed to estimate the error of
 *              (h_i & h_j) when labeling unlabeled data using (h_i & h_j). Out-of-bag estimation
 *              is recommended.
 *
 * Data format: Both the input and output formats are the same as those used by WEKA.
 *
 * ATTN2:       This package was developed by Mr. Ming Li (lim@lamda.nju.edu.cn). There
 *		          is a ReadMe file provided for roughly explaining the codes. But for any
 *		          problem concerning the code, please feel free to contact with Mr. Li.
 *
 */

import weka.classifiers.Classifier;

public interface ErrorMeasurement
{
    public double measureError(Classifier c1, Classifier c2);
}
