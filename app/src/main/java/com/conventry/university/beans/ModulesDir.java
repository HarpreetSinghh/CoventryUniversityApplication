package com.conventry.university.beans;


import com.conventry.university.utils.FileType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModulesDir implements Serializable {

    private String folderTitle;

    private FileType fileType;

    private List<ModulesFiles> files;

    public ModulesDir(){
    }

    public ModulesDir(String folderTitle, FileType fileType) {
        this.folderTitle = folderTitle;
        this.fileType = fileType;
    }

    public String getFolderTitle() {
        return folderTitle;
    }

    public void setFolderTitle(String folderTitle) {
        this.folderTitle = folderTitle;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void setFiles(List<ModulesFiles> files) {
        this.files = files;
    }

    public List<ModulesFiles> getFiles() {
        return this.files;
    }

    public static List<ModulesDir> getModuleFiles() {

        List<ModulesDir> modules = new ArrayList<>();

        // module 1 - 210CT - Programming
        ModulesDir modulesDir= new ModulesDir("210CT - Programming", FileType.DIRECTORY);
        List<ModulesFiles> modulesFiles = new ArrayList<>();

        modulesFiles.add(new ModulesFiles("Week 0 - Introduction.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 1 - Module Introduction.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 3 - Eight Queens.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 4 - Reasoning.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 5 - Efficiency.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 6 - Recursion.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 7 - Data Structures.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 8 - Tress.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 9 - More Trees.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 10 - Graphs.pdf","210CT - Programming",FileType.PDF_FILE));
        modulesDir.setFiles(modulesFiles);
        modules.add(modulesDir);

        // module 2 - 260CT - Software Engineering
        modulesDir = new ModulesDir("260CT - Software Engineering", FileType.DIRECTORY);
        modulesFiles =  new ArrayList<>();

        modulesFiles.add(new ModulesFiles("Week 1 - Software Engineering Overview.pdf","260CT - Software Engineering",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 2 - Requirements and Analysis Modelling .pdf","260CT - Software Engineering",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 3 - OO Software Design and Principles.pdf","260CT - Software Engineering",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 4 - Architectural Design Patterns .pdf","260CT - Software Engineering",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 5 - Design Patterns .pdf","260CT - Software Engineering",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 6 - Data Mapping and Management .pdf","260CT - Software Engineering",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 7 - Anti Design Patterns and Design by Contract .pdf","260CT - Software Engineering",FileType.PDF_FILE));

        modulesDir.setFiles(modulesFiles);
        modules.add(modulesDir);


        // module 3 - 303CEM - Android Application Development
        modulesDir = new ModulesDir("303CEM - Android Application Development", FileType.DIRECTORY);
        modulesFiles =  new ArrayList<>();

        modulesFiles.add(new ModulesFiles("01 Android Studio slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("02 The Java Language slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("03 XML and Gradle slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("04 Simple Views and Layouts slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("05 AdapterViews and Fragments slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("06 Data Persistence slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("07 Testing slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("08 Graphics and Animation slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("09 Services and Wearables slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("10 Location Services and Maps slides.pdf","303CEM - Android Application Development",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("11 create video.pdf","303CEM - Android Application Development",FileType.PDF_FILE));

        modulesDir.setFiles(modulesFiles);
        modules.add(modulesDir);

        // module 4 - 340CT - Software Quality
        modulesDir = new ModulesDir("340CT - Software Quality", FileType.DIRECTORY);
        modulesFiles =  new ArrayList<>();

        modulesFiles.add(new ModulesFiles("Week 1 - Module Introduction .pdf","340CT - Software Quality",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 2 -Scrum Planning and Tracking .pdf","340CT - Software Quality",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 3 - Architecture Patterns .pdf","340CT - Software Quality",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 4 - Agile Software Estimation Techniques .pdf","340CT - Software Quality",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 5 - Software Quality Metrics 2.pdf","340CT - Software Quality",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 6 - Software quality measurement and indicators 2 .pdf","340CT - Software Quality",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 7 - Software quality metrics and ISO_IEC standard .pdf","340CT - Software Quality",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Week 8 - Software Architecture and Quality Criteria.pdf","340CT - Software Quality",FileType.PDF_FILE));

        modulesDir.setFiles(modulesFiles);
        modules.add(modulesDir);

        // module 4 - 380CT - Theoritical Aspects of Computer Science
        modulesDir = new ModulesDir("380CT - Theoritical Aspects of Computer Science", FileType.DIRECTORY);
        modulesFiles =  new ArrayList<>();

        modulesFiles.add(new ModulesFiles("Complexity.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Context-Free Languages.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Decidability.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("DFA & NFA.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Introduction.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Limitations of the Regular Languages.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("NFA equiv. DFA & Regular Expressions.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("NP-Completeness.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Problems.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Space Complexity.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Tackling NP-Hard Problems.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));
        modulesFiles.add(new ModulesFiles("Turing Machines.pdf","380CT - Theoritical Aspects of Computer Science",FileType.PDF_FILE));

        modulesDir.setFiles(modulesFiles);
        modules.add(modulesDir);

        return modules;
    }

}
