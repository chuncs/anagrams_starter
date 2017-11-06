/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();
    private int wordLength = DEFAULT_WORD_LENGTH;


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            String sortedWord = sortLetters(word);
            if(lettersToWord.containsKey(sortedWord) == false) {
                lettersToWord.put(sortedWord, new ArrayList<String>());
            }
            lettersToWord.get(sortedWord).add(word);

            int wordSize = word.length();
            if(sizeToWords.containsKey(wordSize) == false) {
                sizeToWords.put(wordSize, new ArrayList<String>());
            }
            sizeToWords.get(wordSize).add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base) ? true : false;
    }

    private String sortLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord = sortLetters(targetWord);
        int lengthWord = targetWord.length();

        for(String word : wordList) {
            if (word.length() == lengthWord && sortLetters(word).equals(sortedWord)) {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> goodResult = new ArrayList<String>();
        for(char c = 'a'; c <= 'z'; c++) {
            String sortedWord = sortLetters(word + c);
            if(lettersToWord.containsKey(sortedWord)) {
                result.addAll(lettersToWord.get(sortedWord));
            }
        }

        for(String oneMoreLetterWord: result) {
            if(!oneMoreLetterWord.contains(word)) {
                goodResult.add(oneMoreLetterWord);
            }
        }
        return goodResult;
    }

    public String pickGoodStarterWord() {
        String randomWord;
        while(true) {
            randomWord = wordList.get(random.nextInt(wordList.size()));
            if(getAnagramsWithOneMoreLetter(randomWord).size() >= MIN_NUM_ANAGRAMS) {
                if(randomWord.length() == wordLength) {
                    break;
                }
            }
        }
        wordLength = ++wordLength <= MAX_WORD_LENGTH ? wordLength : MAX_WORD_LENGTH;
        return randomWord;
    }
}
