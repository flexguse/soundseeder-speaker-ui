/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
package org.bubblecloud.ilves;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class wraps Vaadin SASS compiler to provide on demand SASS compilation.
 *
 * @author Tommi S.E. Laukkanen
 */
public class IlvessSassCompiler {

    private static final Logger log = LoggerFactory.getLogger(IlvessSassCompiler.class);
    
    /*TODO: main() makes problems with spring boot since spring boot accepts only one single class containing the main method 
     
    public static void main(final String args[]) {
        final String sourceFile;
        final String targetFile;
        final String watchDirectory;

        if (args.length == 3) {
            sourceFile = args[0];
            targetFile = args[1];
            watchDirectory = args[2];
        } else {
            sourceFile = Paths.get("src/main/resources/VAADIN/themes/soundseeder/styles.scss").toString();
            targetFile = Paths.get("target/classes/VAADIN/themes/soundseeder/styles.css").toString();
            watchDirectory = Paths.get("src/main/resources/VAADIN/themes/soundseeder").toString();
        }

        log.info("SASS compilation source file: " + sourceFile);
        log.info("SASS compilation target file: " + targetFile);

        final Runnable runnable = new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    final long startTimeMillis = System.currentTimeMillis();
                    com.vaadin.sass.SassCompiler.main(new String[]{sourceFile, targetFile});
                    log.info("SASS compilation done in " + (System.currentTimeMillis() - startTimeMillis) + "ms.");
                } catch (Exception e) {
                    log.error("Error in SASS compilation: " + e);
                }
            }
        };

        try {
            watch(runnable, ".scss", watchDirectory);
        } catch (Exception e) {
            log.error("Error starting sass file watcher: " + e);
        }
    }
    */
    
    /**
     * Create directory watch for
     * @param runnable the runnable to execute on file change
     * @param fileExtension the file extension.
     * @param directories the directories
     * @throws Exception
     */
    public static void watch(final Runnable runnable, final String fileExtension, final String... directories) throws Exception {
        final WatchService watcher = FileSystems.getDefault().newWatchService();
        for (final String directory : directories) {
            Path path = Paths.get(directory);
            path.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            log.info("SASS source file watch registered for dir: " + path.toString());
        }

        while (true) {
            final WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                final WatchEvent.Kind<?> kind = event.kind();

                @SuppressWarnings("unchecked")
                final WatchEvent<Path> ev = (WatchEvent<Path>) event;
                final Path fileName = ev.context();

                if (kind == StandardWatchEventKinds.ENTRY_MODIFY && fileName.toString().endsWith(fileExtension)) {
                    log.info("SASS compilation started after source file change...");
                    runnable.run();
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}