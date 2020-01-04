package projects.storage.data;

import projects.storage.model.FileData;

import java.nio.file.Paths;
import java.util.UUID;

public interface IFileData {

    default byte[] getDefaultFileContent(){
        return new byte[]{1, 2, 3};
    }

    default byte[] getUpdatedFileContent(){
        return new byte[]{1, 3};
    }

    default UUID generateUuid(){
        return UUID.randomUUID();
    }

    default String getDefaultFileExtension(){
        return "jpg";
    }

    default String getDefaultDirectory(){
        return Paths.get(System.getProperty("user.home"), "uDrink","test").toString();
    }

    default FileData getDefaultFileData(UUID uuid){
        FileData data = new FileData(
                uuid,
                getDefaultFileExtension(),
                getDefaultDirectory()
        );
        data.setId(1L);
        return data;
    }
}
