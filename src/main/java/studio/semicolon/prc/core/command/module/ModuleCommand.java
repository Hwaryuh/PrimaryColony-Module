package studio.semicolon.prc.core.command.module;

import io.quill.paper.command.builder.QuillCommand;
import io.quill.paper.command.builder.QuillCommandBuilder;

public class ModuleCommand {
    public static QuillCommand create() {
        return QuillCommandBuilder.create()
                .name("module")
                .child(BuildChild.create())
                .child(ResetChunkChild.create())
                .child(ResetAdvancementChild.create())
                .child(DebugChild.create())
                .build();
    }
}