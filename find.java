public class find {
    public static String PROGRAM_NAME = "find"; // Nombre del programa

    public static void main(String[] args) throws Exception {
        Kernel.initialize(); // Inicializa el kernel del simulador de sistema de archivos

        find(args[0]);

        Kernel.exit(0); // Termina el programa con un estado de éxito
    }

    // Función para buscar archivos y directorios de forma recursiva
    private static void find(String path) throws Exception {
        int status = 0;
        Stat stat = new Stat(); // Estructura para almacenar información sobre archivos

        // Obtiene información sobre el archivo o directorio en 'path'
        status = Kernel.stat(path, stat);
        if (status < 0) {
            Kernel.perror(PROGRAM_NAME); // Muestra un mensaje de error
            Kernel.exit(1); // Termina el programa con un estado de error
        }

        short type = (short)(stat.getMode() & Kernel.S_IFMT); // Obtiene el tipo de archivo

        if (type == Kernel.S_IFREG) {
            printFileInfo(path, stat); // Si es un archivo regular, muestra su información
        } else if (type == Kernel.S_IFDIR) {
            // Si es un directorio, abre el directorio y lee su contenido
            int fd = Kernel.open(path, Kernel.O_RDONLY); // Abre el directorio en modo lectura
            //Si la operacion de apertura falla muestra un error
            if (fd < 0) {
                Kernel.perror(PROGRAM_NAME); // Muestra un mensaje de error
                System.err.println(PROGRAM_NAME + ": unable to open \"" + path + "\" for reading");
                Kernel.exit(1); // Termina el programa con un estado de error
            }

            DirectoryEntry directoryEntry = new DirectoryEntry(); // Estructura para almacenar entradas de directorio

            while (true) {
                // Lee una entrada del directorio; sale del bucle si hay un error o no se lee nada
                status = Kernel.readdir(fd, directoryEntry);
                if (status <= 0) break;

                String entryName = directoryEntry.getName(); // Obtiene el nombre de la entrada

                // Llama a 'find' de forma recursiva para explorar el contenido del directorio
                status = Kernel.stat(path + "/" + entryName, stat); // Obtiene información sobre la entrada
                if (status < 0) {
                    Kernel.perror(PROGRAM_NAME); // Muestra un mensaje de error
                    Kernel.exit(1); // Termina el programa con un estado de error
                }

                if (!entryName.equals(".") && !entryName.equals("..")) {
                    // Imprime la ruta completa de la entrada y llama a 'find' recursivamente
                    System.out.println(path + "/" + entryName);
                    find(path + "/" + entryName);
                }
            }

            if (status < 0) {
                Kernel.perror("main"); // Muestra un mensaje de error
                System.err.println("main: unable to read directory entry from /");
                Kernel.exit(2); // Termina el programa con un estado de error
            }

            Kernel.close(fd); // Cierra el directorio
        }
    }

    // Función para imprimir información sobre un archivo
    private static void printFileInfo(String name, Stat stat) {
        StringBuffer outputBuffer = new StringBuffer(); // Buffer para almacenar la salida
        String tempString = null;

        // Agrega el número de inodo en un campo de 5 caracteres
        tempString = Integer.toString(stat.getIno());
        for (int i = 0; i < 5 - tempString.length(); i++) {
            outputBuffer.append(' ');
        }
        outputBuffer.append(tempString);
        outputBuffer.append(' ');

        // Agrega el tamaño en un campo de 10 caracteres
        tempString = Integer.toString(stat.getSize());
        for (int i = 0; i < 10 - tempString.length(); i++) {
            outputBuffer.append(' ');
        }
        outputBuffer.append(tempString);
        outputBuffer.append(' ');

        outputBuffer.append(name); // Agrega el nombre del archivo

        System.out.println(outputBuffer.toString()); // Imprime la información
    }
}
