/*
 * Licensed to David Pilato (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.pilato.elasticsearch.crawler.fs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provide Scan Statistics
 *
 * @author David Pilato (aka dadoonet)
 */
public class ScanStatistic {
    private int nbDocScan = 0;
    private int nbDocDeleted = 0;
    private String rootPath;
    private String rootPathId;
    private HashMap<String,FileStats> files;

    public ScanStatistic() {
        this.rootPath = "/";
        this.nbDocScan = 0;
        this.nbDocDeleted = 0;
        this.setFiles(new HashMap<String,FileStats>());
    }

    public ScanStatistic(String rootPath) {
        this.rootPath = rootPath;
        this.nbDocScan = 0;
        this.nbDocDeleted = 0;
    }

    /**
     * @return the nbDocScan
     */
    public int getNbDocScan() {
        return nbDocScan;
    }

    /**
     * @param nbDocScan the nbDocScan to set
     */
    public void setNbDocScan(int nbDocScan) {
        this.nbDocScan = nbDocScan;
    }

    /**
     * @return the nbDocDeleted
     */
    public int getNbDocDeleted() {
        return nbDocDeleted;
    }

    /**
     * @param nbDocDeleted the nbDocDeleted to set
     */
    public void setNbDocDeleted(int nbDocDeleted) {
        this.nbDocDeleted = nbDocDeleted;
    }

    /**
     * @return the rootPath
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * @param rootPath the rootPath to set
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * @return the rootPathId
     */
    public String getRootPathId() {
        return rootPathId;
    }

    /**
     * @param rootPathId the rootPathId to set
     */
    public void setRootPathId(String rootPathId) {
        this.rootPathId = rootPathId;
    }

    /**
     * Increment statistic for new files
     */
    public void addFile() {
        this.nbDocScan++;
    }

    /**
     * Increment statistic for deleted files
     */
    public void removeFile() {
        this.nbDocDeleted++;
    }

	/**
	 * @return the files
	 */
	public HashMap<String,FileStats> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(HashMap<String,FileStats> files) {
		this.files = files;
	}
	
	public void addFileStats(FileStats fstats){
		if (this.files == null){
			this.files = new HashMap<String,FileStats>();
		}		
		this.files.put(fstats.getFileName(),fstats);
	}
	
	public FileStats getFileStats(String key){
		if(this.files != null && this.files.containsKey(key)){
			return  this.files.get(key);
		}else{
			return null;
		}
	}

}
