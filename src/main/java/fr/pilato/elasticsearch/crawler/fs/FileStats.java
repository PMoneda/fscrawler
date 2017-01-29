package fr.pilato.elasticsearch.crawler.fs;

import java.time.Instant;
import java.util.Date;

public class FileStats {
	private String fileName;
	private Instant indexedAt;
	private Instant lastModified;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Instant getIndexedAt() {
		return indexedAt;
	}
	public void setIndexedAt(Instant indexedAt) {
		this.indexedAt = indexedAt;
	}
	/**
	 * @return the lastModified
	 */
	public Instant getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Instant lastModified) {
		this.lastModified = lastModified;
	}
	
}
