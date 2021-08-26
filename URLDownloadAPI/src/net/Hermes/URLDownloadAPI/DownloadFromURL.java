package net.Hermes.URLDownloadAPI;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DownloadFromURL {

	public static void update(File folder, String urlString) {
		try {
			
			if (!folder.exists()) {
	            folder.mkdir();
	        }
	        final URL url = new URL(urlString);
	        BufferedInputStream  in = new BufferedInputStream(url.openStream());
			@SuppressWarnings("resource")
			FileOutputStream fout = new FileOutputStream(folder.getAbsolutePath() + File.separator + "AdRewards.jar");
	
	        final byte[] data = new byte[1024];
	        int count;
	        while ((count = in.read(data, 0, 1024)) != -1) {
	            fout.write(data, 0, count);
	        }
	        for (final File xFile : folder.listFiles()) {
	            if (xFile.getName().endsWith(".zip")) {
	                xFile.delete();
	            }
	        }
	        
	        final File dFile = new File(folder.getAbsolutePath() + File.separator + "AdRewards.jar");
	        if (dFile.getName().endsWith(".zip")) {
	            final File fSourceZip = new File(dFile.getCanonicalPath());
	            final String zipPath = dFile.getCanonicalPath().substring(0, dFile.getCanonicalPath().length() - 4);
	            ZipFile zipFile = new ZipFile(fSourceZip);
	            Enumeration<? extends ZipEntry> e = zipFile.entries();
	            while (e.hasMoreElements()) {
	                ZipEntry entry = e.nextElement();
	                File destinationFilePath = new File(zipPath, entry.getName());
	                destinationFilePath.getParentFile().mkdirs();
	                if (entry.isDirectory()) {
	                    continue;
	                } else {
	                    final BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
	                    int b;
	                    final byte buffer[] = new byte[1024];
	                    final FileOutputStream fos = new FileOutputStream(destinationFilePath);
	                    final BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
	                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
	                        bos.write(buffer, 0, b);
	                    }
	                    bos.flush();
	                    bos.close();
	                    bis.close();
	                }
	                entry = null;
	                destinationFilePath = null;
	            }
	            e = null;
	            zipFile.close();
	            zipFile = null;
	            for (final File dFile1 : new File(zipPath).listFiles()) {
	                if (dFile1.isDirectory()) {
                        final File oFile = new File(folder, dFile.getName());
                        final File[] contents = oFile.listFiles();
                        for (final File cFile : dFile1.listFiles())
                        {
                            boolean found = false;
                            for (final File xFile : contents)
                            {
                                if (xFile.getName().equals(cFile.getName())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                cFile.renameTo(new File(oFile.getCanonicalFile() + File.separator + cFile.getName()));
                            } else {
                                cFile.delete();
                            }
                        }
	                }
	                dFile.delete();
	            }
	            new File(zipPath).delete();
	            fSourceZip.delete();
	        }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
}
