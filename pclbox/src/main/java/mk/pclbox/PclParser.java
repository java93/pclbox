package mk.pclbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * The {@link PclParser} parses the given {@link InputStream} and interprets the printer commands.
 */
public final class PclParser implements AutoCloseable {

    // TODO Maybe we don't need this special PclInputStream (we may just need the InputStream)...
    private final PclInputStream stream;
    private final PrinterCommandHandler commandHandler;
    private final boolean closeStream;

    /**
     * Constructor that internally creates a {@link FileInputStream} for reading and seeking
     * within the PCL data stream.
     *
     * @param inputFile - the {@link File} that contains the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for every {@link PrinterCommand}.
     */
    public PclParser(final File inputFile, final PrinterCommandHandler commandHandler) throws FileNotFoundException {
        this(new FileInputStream(inputFile), commandHandler, true);
    }

    /**
     * Constructor that internally creates a {@link FileInputStream} for reading and seeking
     * within the PCL data stream.
     *
     * @param inputFileName - the name of the file that contains the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for every {@link PrinterCommand}.
     */
    public PclParser(final String inputFileName, final PrinterCommandHandler commandHandler)
            throws FileNotFoundException {
        this(new FileInputStream(inputFileName), commandHandler, true);
    }

    /**
     * Constructor that uses the ready to use {@link PclInputStream} for reading and seeking within
     * the PCL data stream.
     *
     * @param input - the {@link PclInputStream} that will be used to read the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for every {@link PrinterCommand}.
     */
    public PclParser(final PclInputStream input, final PrinterCommandHandler commandHandler) {
        this(input, commandHandler, false);
    }

    /**
     * Constructor that uses the ready to use {@link PclInputStream} for reading and seeking within
     * the PCL data stream.
     *
     * @param input - the {@link PclInputStream} that will be used to read the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for the parsed {@link PrinterCommand}.
     * @param closeStream - true if the {@link PclInputStream} should be closed by the {@link PclParser}.
     */
    public PclParser(final PclInputStream input, final PrinterCommandHandler commandHandler,
            final boolean closeStream) {
        this.stream = input;
        this.commandHandler = commandHandler;
        this.closeStream = closeStream;
    }

    /**
     * Constructor that uses a {@link FileInputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link FileChannel} that is provided by the {@link FileInputStream}.
     *
     * @param input - the {@link FileInputStream} that will be used to read the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for every {@link PrinterCommand}.
     */
    public PclParser(final FileInputStream input, final PrinterCommandHandler commandHandler) {
        this(new PclInputStreamForInputStream(input), commandHandler, false);
    }

    /**
     * Constructor that uses a {@link FileInputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link FileChannel} that is provided by the {@link FileInputStream}.
     *
     * @param input - the {@link FileInputStream} that will be used to read the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for every {@link PrinterCommand}.
     * @param closeStream - true if the {@link PclInputStream} should be closed by the {@link PclParser}.
     */
    public PclParser(final FileInputStream input, final PrinterCommandHandler commandHandler,
            final boolean closeStream) {
        this.stream = new PclInputStreamForInputStream(input);
        this.commandHandler = commandHandler;
        this.closeStream = closeStream;
    }

    /**
     * Constructor that uses a base {@link InputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link InputStream#reset()} and {@link InputStream#skip(long)} that may not
     * be very fast, depending of the implementation. Also note that you must not invoke {@link InputStream#mark()},
     * {@link InputStream#reset()} or {@link InputStream#skip(long)} on the given stream.
     *
     * @param input - the {@link InputStream} that will be used to read the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for every {@link PrinterCommand}.
     */
    public PclParser(final InputStream input, final PrinterCommandHandler commandHandler) {
        this(new PclInputStreamForInputStream(input), commandHandler, false);
    }

    /**
     * Constructor that uses a base {@link InputStream} for reading and seeking within the PCL data stream.
     * Seeking is done by using a {@link InputStream#reset()} and {@link InputStream#skip(long)} that may not
     * be very fast, depending of the implementation. Also note that you must not invoke {@link InputStream#mark()},
     * {@link InputStream#reset()} or {@link InputStream#skip(long)} on the given stream.
     *
     * @param input - the {@link InputStream} that will be used to read the PCL data stream.
     * @param commandHandler - the {@link PrinterCommandHandler} that is invoked for every {@link PrinterCommand}.
     * @param closeStream - true if the {@link PclInputStream} should be closed by the {@link PclParser}.
     */
    public PclParser(final InputStream input, final PrinterCommandHandler commandHandler, final boolean closeStream) {
        this.stream = new PclInputStreamForInputStream(input);
        this.commandHandler = commandHandler;
        this.closeStream = closeStream;
    }

    /**
     * Parses the data stream. For every parsed {@link PrinterCommand} the {@link PrinterCommandHandler} is invoked.
     */
    public void parse() throws IOException, PclException {
        final int lastReadBye = new Pcl5Parser(new PclParserContext(this.stream, this.commandHandler)).parse();
        if (lastReadBye != -1) {
            throw new PclException(
                    "The Pcl5Parser unexpectedly returned before the end of the data stream has been reached");
        }
    }

    @Override
    public void close() throws IOException {
        if (this.closeStream) {
            this.stream.close();
        }
    }
}
