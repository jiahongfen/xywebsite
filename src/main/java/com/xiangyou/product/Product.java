/**
 *
 */
package com.xiangyou.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingli
 *
 */
public class Product {

    private List<Image> slides = new ArrayList<Image>();
    private List<Image> features = new ArrayList<Image>();

    public static class Image {
        String image;
        String name;
        String desc;

        public Image(String image, String name, String desc) {
            this.image = image;
            this.name = name;
            this.desc = desc;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    private String id;
    private String name;
    private String intro;
    private String banner;
    private String brief;
    private String highlights;
    private String fee;
    private String date;
    private String feePlan;
    private long timestamp;

    private List<Day> journey = new ArrayList<Day>();

    public static class Day {
        int index;
        String title;
        StringBuilder details = new StringBuilder();
        Map<String, String> pictures = new HashMap<String, String>();

        public Day() {
        }

        public Day(int index, String title, String details) {
            this.index = index;
            this.title = title;
            this.details.append(details);
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetails() {
            return details.toString();
        }

        public void setDetails(String details) {
            this.details.append(details);
        }

        public Map<String, String> getPictures() {
            return pictures;
        }

        public void setPictures(Map<String, String> pictures) {
            this.pictures = pictures;
        }

        public void addPicture(String name, String image) {
            this.pictures.put(name, image);
        }

        public void addAllPictures(Map<String, String> pictures) {
            this.pictures.putAll(pictures);
        }

        public void appendDetail(String detail) {
            this.details.append(detail).append("<br />");
        }
    }

    /**
     *
     */
    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Image> getSlides() {
        return slides;
    }

    public void setSlides(List<Image> slides) {
        this.slides = slides;
    }

    public List<Image> getFeatures() {
        return features;
    }

    public void setFeatures(List<Image> features) {
        this.features = features;
    }

    public List<Day> getJourney() {
        return journey;
    }

    public void setJourney(List<Day> journey) {
        this.journey = journey;
    }

    public void addSlide(Image image) {
        slides.add(image);
    }

    public void addFeature(Image image) {
        features.add(image);
    }

    public int addJourneyDay(Day day) {
        journey.add(day);
        int index = journey.size();
        day.setIndex(index);
        return index;
    }

    public String getFeePlan() {
        return feePlan;
    }

    public void setFeePlan(String feePlan) {
        this.feePlan = feePlan;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
