package fr.pilato.elasticsearch.crawler.fs.fileabstractor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.maven.scm.provider.svn.util.SvnUtil;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import fr.pilato.elasticsearch.crawler.fs.meta.settings.FsSettings;
import fr.pilato.elasticsearch.crawler.fs.meta.settings.Server;
import fr.pilato.elasticsearch.crawler.fs.util.FsCrawlerUtil;

public class FileAbstractorSVN extends FileAbstractor<SVNDirEntry> {

	
	private SVNRepository repository = null;
	private Collection<FileAbstractModel> filesToIndex;
	public FileAbstractorSVN(FsSettings fsSettings) {
		super(fsSettings);
		filesToIndex = new LinkedList<FileAbstractModel>();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@Override
	public FileAbstractModel toFileAbstractModel(String path, SVNDirEntry entry) {
		// TODO Auto-generated method stub
		FileAbstractModel m = new FileAbstractModel();
		m.directory = entry.getKind() == SVNNodeKind.DIR;
		m.file = !m.directory;
		try {			
			m.fullpath = path;
			m.path =  path;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return new FileAbstractModel();
		}		
		m.name = entry.getName();
		m.owner = entry.getAuthor();
		try{
			m.lastModifiedDate = entry.getDate().toInstant();
		}catch(Exception e){
			
		}
		m.size = entry.getSize();
		return m;
	}

	@Override
	public InputStream getInputStream(FileAbstractModel file) throws Exception {
		// TODO Auto-generated method stub
		SVNProperties fileProperties = new SVNProperties( );
        ByteArrayOutputStream baos = new ByteArrayOutputStream( );        
        repository.getFile( file.path , -1 , fileProperties , baos );
        ByteArrayInputStream input = new ByteArrayInputStream(baos.toByteArray());
		return input;
	}

	@Override
	public Collection<FileAbstractModel> getFiles(String path) throws Exception {		
		Collection<FileAbstractModel> files = new LinkedList<FileAbstractModel>();
		try{
			Collection entries = repository.getDir( path, -1 , null , (Collection) null );
	        Iterator iterator = entries.iterator( );
	        
	        while ( iterator.hasNext( ) ) {
	            SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );
	            String p = (path+"/"+entry.getName()).replaceAll("//","/");
	            logger.info(p);
	            if(entry.getKind() == SVNNodeKind.DIR){
	            	if(FsCrawlerUtil.isIndexable(p, fsSettings.getFs().getIncludesDir(), fsSettings.getFs().getExcludes())){
	            		//logger.info("------------------------------------------");
	            		logger.info("Diretorio indexado");
	            		//logger.info(entry.getName());
	            		//logger.info("------------------------------------------");
	            		files.add(toFileAbstractModel(p, entry));
	            	}
	            }else{
	            	if(FsCrawlerUtil.isIndexable(p, fsSettings.getFs().getIncludes(), fsSettings.getFs().getExcludes())){
	            		//logger.info("------------------------------------------");
	            		logger.info("Arquivo indexado");
	            		//logger.info(entry.getName());
	            		//logger.info("------------------------------------------");
	            		files.add(toFileAbstractModel(p, entry));
	            	}
	            }
	        	
	            
	        }
	        
		}catch(Exception e){
			
		}
		return files;
        
	}

	@Override
	public boolean exists(String path) throws Exception {
		return true;		
	}

	@Override
	public void open() throws Exception {
		Server s = this.fsSettings.getServer();				
		repository = SVNRepositoryFactory.create( SVNURL.parseURIEncoded( s.getHostname() ) );
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( s.getUsername() , s.getPassword() );
		repository.setAuthenticationManager( authManager );
		//listEntries(repository, "/");
	}
	
	
	public void listEntries( SVNRepository repository, String path ) throws SVNException {
		Collection entries = repository.getDir( path, -1 , null , (Collection) null );
        Iterator iterator = entries.iterator( );        
        while ( iterator.hasNext( ) ) {
            SVNDirEntry entry = ( SVNDirEntry ) iterator.next( );
            if ( entry.getKind() == SVNNodeKind.DIR ) {
                listEntries( repository, ( path.equals( "" ) ) ? entry.getName( ) : path + "/" + entry.getName( ) );
            }else{
            	if(FsCrawlerUtil.isIndexable(entry.getName(), fsSettings.getFs().getIncludes(), fsSettings.getFs().getExcludes())){
            		logger.info("------------------------------------------");
            		logger.info("Arquivo para indexar");
            		logger.info(entry.getName());
            		logger.info("------------------------------------------");
            		this.filesToIndex.add(toFileAbstractModel(path, entry));
            	}
            	
            }
            
        }
    }

	@Override
	public void close() throws Exception {
		if(this.repository != null){
			this.repository.closeSession();
		}
	}

}
