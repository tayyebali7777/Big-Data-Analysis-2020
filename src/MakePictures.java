import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.chart.ChartUtilities; 
public class MakePictures {
	Data data;
	public void makeBoxFigure(double sigma) throws IOException {
		data=new Data("data/trip_data_1.csv");
		System.out.println("Before prepare data:"+sigma);
        data.prepareData(sigma);

		BoxAndWhiskerCategoryDataset dataset = createRawDataset();

        CategoryAxis xAxis = new CategoryAxis("Type");
        NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        //renderer.setFillBox(false);
        //renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        JFreeChart chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        File file=new File("BeforeRefreshRaw"+sigma+".png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
        
        
        dataset = createKNormDataset();
        xAxis = new CategoryAxis("Type");
        yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        renderer = new BoxAndWhiskerRenderer();
        //renderer.setFillBox(false);
        //renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        file=new File("BeforeRefreshMNorm"+sigma+".png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
        
        dataset = createMNormDataset();
        xAxis = new CategoryAxis("Type");
        yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        renderer = new BoxAndWhiskerRenderer();
        //renderer.setFillBox(false);
        //renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        file=new File("BeforeRefreshMNorm"+sigma+".png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
        
        
        System.out.println("After prepare data:"+sigma);
        int leng1=data.longitude_k_zero.size();
        data.refreshData(sigma*1e8);
        System.out.println("Or:"+leng1);
        dataset = createRawDataset();
        xAxis = new CategoryAxis("Type");
        yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        renderer = new BoxAndWhiskerRenderer();
        //renderer.setFillBox(false);
        //renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        file=new File("AfterRefreshRaw"+sigma+".png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
        
        dataset = createKNormDataset();
        xAxis = new CategoryAxis("Type");
        yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        renderer = new BoxAndWhiskerRenderer();
        //renderer.setFillBox(false);
        //renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        file=new File("AfterRefreshKNorm"+sigma+".png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
        
        dataset = createMNormDataset();
        xAxis = new CategoryAxis("Type");
        yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        renderer = new BoxAndWhiskerRenderer();
        //renderer.setFillBox(false);
        //renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        file=new File("AfterRefreshNorm"+sigma+".png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
        
	}
	
	public void boxplotCount() throws IOException {
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		BufferedReader bReader=new BufferedReader(new FileReader(new File("data/characterCounts.csv")));
		String lineString=bReader.readLine();
		ArrayList<Integer> aList=new ArrayList<Integer>();
		while(lineString!=null) {
			lineString=lineString.replace("\n", "");
			String [] srStrings=lineString.split(",");
			aList.add(Integer.parseInt(srStrings[srStrings.length-1]));
			lineString=bReader.readLine();
		}
		dataset.add(aList, "", "");
        CategoryAxis xAxis = new CategoryAxis("Type");
        NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(false);
        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        //renderer.setFillBox(false);
        //renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        JFreeChart chart = new JFreeChart(
            "Box-and-Whisker Demo",
            new Font("SansSerif", Font.BOLD, 14),
            plot,
            true
        );
        File file=new File("countsBoxplot.png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
	}
	public void linePlot(String fileString) throws IOException{
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		BufferedReader bReader=new BufferedReader(new FileReader(new File(fileString)));
		String lineString=bReader.readLine();
		int count=0;
		while(lineString!=null) {
			String [] srStrings=lineString.replace("\n", "").split(",");
			dataset.addValue(Double.parseDouble(srStrings[0]), "", count+"");
			count++;
			lineString=bReader.readLine();
		}
		
		fileString=fileString.replace("l", "").replace(".csv", "");
		final JFreeChart chart = ChartFactory.createLineChart(
	            "Learning rate:"+Double.parseDouble(fileString.replace("data/", "")),       // chart title
	            "Rounds",                    // domain axis label
	            "Value",                   // range axis label
	            dataset,                   // data
	            PlotOrientation.VERTICAL,  // orientation
	            true,                      // include legend
	            true,                      // tooltips
	            false                      // urls
	        );
		chart.setBackgroundPaint(Color.white);
		File file=new File(fileString+".png");
        ChartUtilities.saveChartAsPNG(file, chart, 800, 600); 
	}
	public static void main(String [] args) throws IOException {
		MakePictures mPictures=new MakePictures();
		//mPictures.makeBoxFigure(3);
//		mPictures.linePlot("data/l0.00001.csv");
//		mPictures.linePlot("data/l000.001.csv");
//		mPictures.linePlot("data/l00.0002.csv");
//		mPictures.linePlot("data/l00.0005.csv");
//		mPictures.linePlot("data/l00.0001.csv");
//		mPictures.linePlot("data/l0.00002.csv");
//		mPictures.linePlot("data/l0.00005.csv");
		String [] fileStrings= {"data/l0.0000000000001.csv","data/l0.000000000001.csv","data/l0.00000000001.csv","data/l0.0000000001.csv",
				"data/l0.000000001.csv","data/l0.00000001.csv","data/l0.0000001.csv"};
		for(String n:fileStrings) {
			mPictures.linePlot(n);
		}
		
	}
	public void scatter() {
		final NumberAxis domainAxis = new NumberAxis("X");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Y");
        rangeAxis.setAutoRangeIncludesZero(false);
        final FastScatterPlot plot = new FastScatterPlot(this.data, domainAxis, rangeAxis);
        final JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);
	}
	private BoxAndWhiskerCategoryDataset createRawDataset() throws IOException {
        final DefaultBoxAndWhiskerCategoryDataset dataset 
            = new DefaultBoxAndWhiskerCategoryDataset();
        ArrayList<Double> tmpArrayList;
        for(int i=0;i<3;i++) {
        	switch (i) {
			case 0:
				tmpArrayList=new ArrayList<Double>(data.longitudeArrayList);
				dataset.add(tmpArrayList, "Longitude", "Data Without Norm");
				break;
			case 1:
				tmpArrayList=new ArrayList<Double>(data.latitudeArrayList);
				dataset.add(tmpArrayList, "Latitude", "Data Without Norm");
				break;
			case 2:
				tmpArrayList=new ArrayList<Double>();
				for(Timestamp t:data.timestamps) {
					tmpArrayList.add((double)t.getTime());
				}
				dataset.add(tmpArrayList, "Timestamp", "Data Without Norm");
				break;
			default:
				break;
			}
        }
        return dataset;
    }
	private BoxAndWhiskerCategoryDataset createMNormDataset() throws IOException {
        final DefaultBoxAndWhiskerCategoryDataset dataset 
            = new DefaultBoxAndWhiskerCategoryDataset();
        ArrayList<Double> tmpArrayList;
        for(int i=0;i<3;i++) {
        	switch (i) {
			case 0:
				tmpArrayList=new ArrayList<Double>(data.longitude_min_max_normArrayList);
				dataset.add(tmpArrayList, "Longitude", "Min Max Norm");
				break;
			case 1:
				tmpArrayList=new ArrayList<Double>(data.latitude_min_max_normArrayList);
				dataset.add(tmpArrayList, "Latitude", "Min Max Norm");
				break;
			case 2:
				tmpArrayList=new ArrayList<Double>(data.timestamp_min_max_normArrayList);
				dataset.add(tmpArrayList, "Timestamp", "Min Max Norm");
				break;
			default:
				break;
			}
        }
        return dataset;
    }
	private BoxAndWhiskerCategoryDataset createKNormDataset() throws IOException {
        final DefaultBoxAndWhiskerCategoryDataset dataset 
            = new DefaultBoxAndWhiskerCategoryDataset();
        ArrayList<Double> tmpArrayList;
        for(int i=0;i<3;i++) {
        	switch (i) {
			case 0:
				tmpArrayList=new ArrayList<Double>(data.longitude_k_zero);
				dataset.add(tmpArrayList, "Longitude", "K-Zero Norm");
				break;
			case 1:
				tmpArrayList=new ArrayList<Double>(data.latitude_k_zero);
				dataset.add(tmpArrayList, "Latitude", "K-Zero Norm");
				break;
			case 2:
				tmpArrayList=new ArrayList<Double>(data.timestamp_k_zero);
				dataset.add(tmpArrayList, "Timestamp", "K-Zero Norm");
				break;
			default:
				break;
			}
        }
        return dataset;
    }
//private BoxAndWhiskerCategoryDataset createSampleDataset() {
//        
//        final int seriesCount = 3;
//        final int categoryCount = 4;
//        final int entityCount = 22;
//        
//        final DefaultBoxAndWhiskerCategoryDataset dataset 
//            = new DefaultBoxAndWhiskerCategoryDataset();
//        for (int i = 0; i < seriesCount; i++) {
//            for (int j = 0; j < categoryCount; j++) {
//                final List list = new ArrayList();
//                // add some values...
//                for (int k = 0; k < entityCount; k++) {
//                    final double value1 = 10.0 + Math.random() * 3;
//                    list.add(new Double(value1));
//                    final double value2 = 11.25 + Math.random(); // concentrate values in the middle
//                    list.add(new Double(value2));
//                }
//             
//                System.out.println(list);
//                dataset.add(list, "Series " + i, " Type " + j);
//            }
//            
//        }
//
//        return dataset;
//    }

}
