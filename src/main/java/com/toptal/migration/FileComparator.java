package com.toptal.migration;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.cloudaxis.agsc.portal.model.Caregiver;
import com.toptal.migration.model.CandidateDocument;
import com.toptal.migration.model.CandidateDocumentMapping;

/**
 * <code>FileComparator</code>
 *
 * @author Eduard Napolov
 * @version 1.0
 */
public class FileComparator
{
    public static void main(String[] args) throws IOException
    {
        String oldFileOne = args[0];
        String oldFileTwo = args[1];
        String candidateFile = args[2];
        String newFile = args[3];

        HashMap<String, List<CandidateDocument>> oldFiles = loadOldFiles(oldFileOne, oldFileTwo);
        HashMap<String, List<CandidateDocumentMapping>> newFiles = loadNewFile(newFile);

        HashMap<String, String> candidates = loadCandidates(candidateFile);

        System.out.println("Loaded original data for "+oldFiles.size()+" candidates");
        System.out.println("Loaded new data for "+newFiles.size()+" candidates");

        System.out.println("ProspectId,Prospect Name,Original document count,Updated document count");
        for(String candidateId : newFiles.keySet()){
            List<CandidateDocumentMapping> mappings = newFiles.get(candidateId);
            List<CandidateDocument> oldDocuments = oldFiles.get(candidateId);
            if(mappings.size() != oldDocuments.size()){
                System.out.println(candidateId+","+candidates.get(candidateId)+","+oldDocuments.size()+","+mappings.size());
            }
        }
    }

    private static HashMap<String, String> loadCandidates(String fileName)
            throws IOException
    {
        HashMap<String, String> candidates = new HashMap<>();
        try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileName), 1024 * 1024);
            Scanner scanner = new Scanner(inputStream)){
            while (scanner.hasNextLine()){
                String nextLine = scanner.nextLine();
                if(nextLine.isEmpty()){
                    continue;
                }
                String[] tokens = nextLine.split(";");
                String id = tokens[0];
                String name = tokens[5];

                candidates.put(id, name);
            }
        }
        return candidates;
    }
private static HashMap<String, List<CandidateDocumentMapping>> loadNewFile(String newFile)
            throws IOException
    {
        HashMap<String, List<CandidateDocumentMapping>> files = new HashMap<>();
        try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(newFile), 1024 * 1024);
            Scanner scanner = new Scanner(inputStream)){
            while (scanner.hasNextLine()){
                String nextLine = scanner.nextLine();
                if(nextLine.isEmpty()){
                    continue;
                }
                String[] tokens = nextLine.split(";");
                CandidateDocumentMapping mapping = new CandidateDocumentMapping(tokens);
                List<CandidateDocumentMapping> candidateMappings = files.computeIfAbsent(mapping.getProspectId(),
                                                                                   k -> new ArrayList<>(10));
                candidateMappings.add(mapping);
            }
        }
        return files;
    }

    private static HashMap<String, List<CandidateDocument>> loadOldFiles(String oldFileOne, String oldFileTwo)
            throws IOException
    {
        HashMap<String, List<CandidateDocument>> files = new HashMap<>();

        addDocuments(oldFileOne, files);
        addDocuments(oldFileTwo, files);

        return files;
    }

    private static void addDocuments(String oldFileOne, HashMap<String, List<CandidateDocument>> files)
            throws IOException
    {
        try(BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(oldFileOne), 1024 * 1024);
            Scanner scanner = new Scanner(inputStream)){
            while (scanner.hasNextLine()){
                String nextLine = scanner.nextLine();
                if(nextLine.isEmpty()){
                    continue;
                }
                String[] tokens = nextLine.split(",");
                CandidateDocument document = new CandidateDocument(tokens);
                List<CandidateDocument> candidateDocuments = files.computeIfAbsent(document.getProspectId(),
                                                                                   k -> new ArrayList<>(10));
                candidateDocuments =
                        candidateDocuments.stream().filter(c -> !c.getFileName().equals(document.getFileName())).collect(
                                Collectors.toList());
                candidateDocuments.add(document);
                files.put(document.getProspectId(), candidateDocuments);
            }

        }
    }
}
