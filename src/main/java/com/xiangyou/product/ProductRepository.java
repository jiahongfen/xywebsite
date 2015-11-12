/**
 * 
 */
package com.xiangyou.product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xiangyou.product.Product.Day;
import com.xiangyou.product.Product.Image;

/**
 * @author dingli
 *
 */
@Repository
@Transactional(readOnly = true)
public class ProductRepository {

    @Autowired
    ServletContext context;

    public static final String EXTENSION_JOURNEY = "journey";
    public static final String PREFIX_DAY = "day";
    public static final String PREFIX_SLIDE = "slide";
    public static final Pattern TITLE_INDEX_PATTERN = Pattern.compile("^\\d+[., ，、]");

    public static final Pattern JOURTAG_PATTERN = Pattern.compile("^<.+>$");

    public static final Pattern FEEPLAN_SUB = Pattern.compile("^.+[:：]$");

    public static final String JOURTAG_NAME = "Name";
    public static final String JOURTAG_INTRO = "Intro";
    public static final String JOURTAG_BRIEF = "Brief";
    public static final String JOURTAG_HIGHLIGHTS = "Highlights";
    public static final String JOURTAG_FEE = "Fee";
    public static final String JOURTAG_DATE = "Date";
    public static final String JOURTAG_JOURPLAN = "JourPlan";
    public static final String JOURTAG_FEEPLAN = "FeePlan";
    public static final String JOURTAG_IMAGES = "Images";

    private Map<String, Product> products;
    private File productsFolder;

    /**
     * 
     */
    public ProductRepository() {
    }

    public synchronized Map<String, Product> getProducts() {
        if (products != null && products.size() > 0) {
            return products;
        }
        products = new LinkedHashMap<String, Product>();
        File[] productFolders = getProductsFolder().listFiles();
        Arrays.sort(productFolders);
        for (File productFolder : productFolders) {
            if (productFolder.isDirectory()) {
                String id = FilenameUtils.getBaseName(productFolder.getName());
                Product product = getProduct(id, productFolder);
                products.put(id, product);
                System.out.println("product is " + product.getId());
            }
        }
        return products;
    }

    public File getProductsFolder() {
        if (productsFolder == null) {
            productsFolder = new File(context.getRealPath("resources"), "products");
        }
        return productsFolder;
    }

    public Product getProduct(String id, File productFolder) {
        Product product = new Product();
        product.setId(id);
        File journeyFile = getJourneyFile(productFolder);
        Map<String, List<String>> journeyInfo = getJourneyInfo(journeyFile);
        List<Image> imageList = getImageList(product, journeyInfo.get(JOURTAG_IMAGES));
        getName(product, journeyInfo.get(JOURTAG_NAME));
        getIntro(product, journeyInfo.get(JOURTAG_INTRO));
        getFee(product, journeyInfo.get(JOURTAG_FEE));
        getDate(product, journeyInfo.get(JOURTAG_DATE));
        getBrief(product, journeyInfo.get(JOURTAG_BRIEF));
        getHighlights(product, journeyInfo.get(JOURTAG_HIGHLIGHTS));
        getFeatures(product, imageList);
        getSlides(product, imageList);
        getJourneyPlan(product, journeyInfo.get(JOURTAG_JOURPLAN), imageList);
        getFeePlan(product, journeyInfo.get(JOURTAG_FEEPLAN));
        product.setBanner(getBanner(id));
        product.setTimestamp(journeyFile.lastModified());
        return product;
    }

    public Product getProduct(String id) {
        Product product = getProducts().get(id);
        if (product != null) {
            File productFolder = new File(getProductsFolder(), id);
            if (product.getTimestamp() != getJourneyFile(productFolder).lastModified()) {
                product = getProduct(id, productFolder);
                products.put(id, product);
            }
        }
        return product;
    }

    private void getIntro(Product product, List<String> intro) {
        product.setIntro(toString(intro));
    }

    private void getFeePlan(Product product, List<String> feePlan) {
        if (feePlan == null || feePlan.isEmpty()) {
            return;
        }
        StringBuilder content = new StringBuilder();
        boolean isLast = false;
        int size = feePlan.size();
        int lastIndex = size - 1;
        for (int i = 0; i < size; i++) {
            String line = feePlan.get(i);
            if (i == lastIndex) {
                isLast = true;
            }
            if (isFeePlanSub(line)) {
                content.append("<h4>").append(line).append("</h4>");
            } else {
                if (!isLast) {
                    content.append(line).append("<br />");
                }
            }
        }
        product.setFeePlan(content.toString());
    }

    private static boolean isFeePlanSub(String line) {
        return FEEPLAN_SUB.matcher(line).matches();
    }

    private void getBrief(Product product, List<String> brief) {
        if (brief == null || brief.isEmpty()) {
            return;
        }
        product.setBrief(toString(brief));
    }

    private void getHighlights(Product product, List<String> highlights) {
        if (highlights == null || highlights.isEmpty()) {
            return;
        }
        product.setHighlights(toString(highlights));
    }

    private void getDate(Product product, List<String> date) {
        if (date == null || date.isEmpty()) {
            return;
        }
        product.setDate(date.get(0));
    }

    private String toString(List<String> lines) {
        StringBuilder content = new StringBuilder();
        boolean isLast = false;
        int size = lines.size();
        int lastIndex = size - 1;
        boolean isFirst = true;
        for (int i = 0; i < size; i++) {
            if (i == lastIndex) {
                isLast = true;
            }
            String line = lines.get(i);
            if (StringUtils.isBlank(line)) {
                if (!isLast) {
                    content.append("<br />");
                }
            } else {
                if (isFirst) {
                    isFirst = false;
                } else {
                    content.append("<br />");
                }
                content.append(line);
            }
        }
        return content.toString();
    }

    private void getFee(Product product, List<String> fee) {
        if (fee == null || fee.isEmpty()) {
            return;
        }
        product.setFee(fee.get(0));
    }

    private List<Image> getImageList(Product product, List<String> images) {
        List<Image> imageList = new ArrayList<Image>();
        if (images == null || images.isEmpty()) {
            return imageList;
        }
        for (String line : images) {
            String[] splits = line.split(" ");
            if (splits.length == 1) {
                Image image = new Image(getProductImage(product.getId(), splits[0]),
                        FilenameUtils.getBaseName(splits[0]), "");
                imageList.add(image);
            } else if (splits.length == 2) {
                Image image = new Image(getProductImage(product.getId(), splits[0]), splits[1], "");
                imageList.add(image);
            } else if (splits.length == 3) {
                Image image = new Image(getProductImage(product.getId(), splits[0]), splits[1], splits[2]);
                imageList.add(image);
            }
        }
        return imageList;
    }

    private Map<String, List<String>> getJourneyInfo(File journeyFile) {
        Map<String, List<String>> journeyInfo = new HashMap<String, List<String>>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(journeyFile));
            String line;
            List<String> content = new ArrayList<String>(0);
            while ((line = in.readLine()) != null) {
                if (isJourTag(line)) {
                    content = new ArrayList<String>();
                    journeyInfo.put(getJourTag(line), content);
                } else {
                    content.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Read journey text io exception", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignore) {
                }
            }
        }
        return journeyInfo;
    }

    private String getJourTag(String line) {
        return line.substring(1, line.length() - 1);
    }

    private boolean isJourTag(String line) {
        return JOURTAG_PATTERN.matcher(line).matches();
    }

    public void getJourneyPlan(Product product, List<String> jourPlan, List<Image> imageList) {
        if (jourPlan == null || jourPlan.isEmpty()) {
            return;
        }
        Map<String, Map<String, String>> journeyPicsMap = getJourneyPicsMap(imageList);
        Product.Day day = new Product.Day();
        for (String line : jourPlan) {
            if (StringUtils.isBlank(line)) {
                if (day.getTitle() != null) {
                    int index = product.addJourneyDay(day);
                    findJourneyPics(day, index, journeyPicsMap);

                    day = new Product.Day();
                }
            } else if (day.getTitle() == null) {
                day.setTitle(TITLE_INDEX_PATTERN.matcher(line).replaceFirst(""));
            } else {
                day.appendDetail(line);
            }
        }
        if (day.getTitle() != null) {
            int index = product.addJourneyDay(day);
            findJourneyPics(day, index, journeyPicsMap);
        }
    }

    private Map<String, Map<String, String>> getJourneyPicsMap(List<Image> imageList) {
        Map<String, Map<String, String>> journeyPicsMap = new HashMap<String, Map<String, String>>();
        for (Image image : imageList) {
            if (image.getName().startsWith(PREFIX_DAY)) {
                String[] fileNames = image.getName().split("_");
                if (fileNames.length != 2) {
                    throw new RuntimeException(image.getName() + " is not a well named journey picture");
                }
                Map<String, String> pictures = journeyPicsMap.get(fileNames[0]);
                if (pictures == null) {
                    pictures = new LinkedHashMap<String, String>();
                    journeyPicsMap.put(fileNames[0], pictures);
                }
                pictures.put(image.getName(), image.getImage());
            }
        }
        return journeyPicsMap;
    }

    @SuppressWarnings("unused")
    private Map<String, Map<String, String>> getJourneyPicsMap(Product product) {
        Map<String, Map<String, String>> journeyPicsMap = new HashMap<String, Map<String, String>>();
        File productFolder = new File(context.getRealPath("resources/products"), product.getId());
        for (File f : productFolder.listFiles()) {
            String fileName = FilenameUtils.getBaseName(f.getName());
            if (fileName.startsWith(PREFIX_DAY)) {
                String[] fileNames = fileName.split("_");
                if (fileNames.length != 2) {
                    throw new RuntimeException(f + " is not a well named journey picture");
                }
                Map<String, String> pictures = journeyPicsMap.get(fileNames[0]);
                if (pictures == null) {
                    pictures = new LinkedHashMap<String, String>();
                    journeyPicsMap.put(fileNames[0], pictures);
                }
                pictures.put(fileNames[1], getProductImage(product.getId(), f.getName()));
            }
        }
        return journeyPicsMap;
    }

    private void findJourneyPics(Day day, int index, Map<String, Map<String, String>> journeyPicsMap) {
        Map<String, String> pictures = journeyPicsMap.get(PREFIX_DAY + index);
        day.addAllPictures(pictures);
        for (Entry<String, String> entry : pictures.entrySet()) {
            day.addPicture(entry.getKey(), entry.getValue());
        }
    }

    private File getJourneyFile(File productFolder) {
        for (File f : productFolder.listFiles()) {
            if (EXTENSION_JOURNEY.equalsIgnoreCase(FilenameUtils.getExtension(f.getName()))) {
                return f;
            }
        }
        throw new RuntimeException("Cannot find journey file");
    }

    public void getName(Product product, List<String> name) {
        product.setName(name.get(0));
    }

    public void getFeatures(Product product, List<Image> imageList) {
        for (Image image : imageList) {
            if (image.getName().startsWith(PREFIX_SLIDE)) {
                product.addFeature(image);
            }
        }
    }

    private void getSlides(Product product, List<Image> imageList) {
        for (Image image : imageList) {
            if (image.getName().startsWith(PREFIX_SLIDE)) {
                product.addSlide(image);
            }
        }
    }

    private String getProductImage(String id, String imageName) {
        StringBuilder image = new StringBuilder();
        image.append("/resources/products/").append(id).append("/").append(imageName).toString();
        return new File(context.getRealPath(image.toString())).exists() ? image.toString() : null;
    }

    public static void main(String[] args) {
        System.out.println(TITLE_INDEX_PATTERN.matcher("1、上海-慕尼黑").replaceFirst(""));
        String briefTag = "<Brief>";
        System.out.println(JOURTAG_PATTERN.matcher(briefTag).matches());
        System.out.println(briefTag.substring(1, briefTag.length() - 1));
        System.out.println(FEEPLAN_SUB.matcher("费用包含：").matches());
    }

    private static String getBanner(String id) {
        return "background-image: url('./resources/products/" + id + "/banner.jpg')";
    }
}
