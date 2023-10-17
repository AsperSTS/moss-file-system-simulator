public class findv2 {
    public static void main(String[] args) throws Exception {
        Kernel.initialize(); // Inicializa el simulador del sistema de archivos.

        find(args[0]); // Llama a la función 'find' para buscar archivos y directorios.

        Kernel.exit(0); // Termina el programa con un estado de éxito.
    }
    private static void find(String path) throws Exception {
        try {
            Stat stat = new Stat();
            int status = Kernel.stat(path, stat); // Obtiene información sobre el archivo o directorio en 'path'.

            if (status < 0) {
                handleError("Unable to get file/directory information for " + path); // Maneja errores al obtener información.
                return;
            }
            // Comprueba si el archivo o directorio es un archivo regular.
            if ((stat.getMode() & Kernel.S_IFMT) == Kernel.S_IFREG) {
                printFileInfo(path, stat); // Muestra información sobre el archivo.
            } 
            // Comprueba si el archivo o directorio es un directorio.
            else if ((stat.getMode() & Kernel.S_IFMT) == Kernel.S_IFDIR) {
                int dirFd = Kernel.open(path, Kernel.O_RDONLY); // Abre el directorio en modo lectura.

                if (dirFd < 0) {
                    handleError("Unable to open directory: " + path); // Maneja errores al abrir el directorio.
                    return;
                }

                DirectoryEntry entry = new DirectoryEntry();

                while (Kernel.readdir(dirFd, entry) > 0) {
                    String entryName = entry.getName();

                    // Evita procesar las entradas "." y ".." para evitar bucles infinitos.
                    if (!entryName.equals(".") && !entryName.equals("..")) {
                        String fullPath = path + "/" + entryName;
                        System.out.println(fullPath); // Imprime la ruta completa de la entrada.
                        find(fullPath); // Llama de forma recursiva a 'find' para explorar el contenido del directorio.
                    }
                }

                Kernel.close(dirFd); // Cierra el directorio después de procesar las entradas.
            }
        } catch (Exception e) {
            handleError("An error occurred: " + e.getMessage()); // Maneja errores generales.
        }
    }

    private static void handleError(String message) throws Exception {
        Kernel.perror("Find"); // Muestra un mensaje de error del simulador de sistema de archivos.
        System.err.println("Find: " + message); // Imprime un mensaje de error personalizado.
        Kernel.exit(1); // Termina el programa con un estado de error.
    }

    private static void printFileInfo(String path, Stat stat) {
        // Imprime información sobre el archivo en un formato específico.
        System.out.printf("%5d %10d %s%n", stat.getIno(), stat.getSize(), path);
    }
}
