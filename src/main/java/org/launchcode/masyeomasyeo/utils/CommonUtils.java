package org.launchcode.masyeomasyeo.utils;

import org.launchcode.masyeomasyeo.models.Song;
import org.launchcode.masyeomasyeo.models.Songs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class CommonUtils {

    public static String getFileExtension(String name) {
        if(name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0) {
            return name.substring(name.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    /**
     * read csv file
     * @param filePath
     * @return
     */
    public static List<Song> readCsv(String filePath){
        List<Song> list = new ArrayList<Song>();
        BufferedReader buff = null;
        String line = "";
        String splitBy = ",";

        try {
            buff = new BufferedReader(new FileReader(filePath));
            while((line = buff.readLine()) != null) {
                String[] data = line.split(splitBy);
                Song song = new Song();
                //song.setId(Integer.valueOf(data[0]));
                song.setName(data[0]);
                list.add(song);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buff != null) {
                try {
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * read xml file
     * @param filePath
     * @return
     * @throws JAXBException
     */
    public static List<Song> readXml(String filePath) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(Songs.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Songs songs = (Songs) unmarshaller.unmarshal(new File(filePath));

        return songs.getSongs();
    }
}