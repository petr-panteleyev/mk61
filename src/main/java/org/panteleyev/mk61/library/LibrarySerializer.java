// Copyright © 2026 Petr Panteleyev
// SPDX-License-Identifier: GPL-3.0-only
package org.panteleyev.mk61.library;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.panteleyev.mk61.jaxb.Mk61Program;
import org.panteleyev.mk61.jaxb.ObjectFactory;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import static org.panteleyev.mk61.engine.DeviceModel.PROGRAM_MEMORY_SIZE;

public class LibrarySerializer {
    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();
    private static final String XSD_PROGRAM = "/xsd/mk61_program.xsd";

    public static Program loadProgram(InputStream inputStream) {
        try {
            var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            var schema = schemaFactory.newSchema(LibrarySerializer.class.getResource(XSD_PROGRAM));
            var jaxbContext = JAXBContext.newInstance(Mk61Program.class);
            var unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            var mk61Program = unmarshaller.unmarshal(new StreamSource(inputStream), Mk61Program.class).getValue();

            var memory = new int[PROGRAM_MEMORY_SIZE];
            Arrays.fill(memory, 0);

            List<MemoryCell> steps = null;

            if (mk61Program.getMemory() != null) {
                for (int index = 0; index < mk61Program.getMemory().getCell().size(); index++) {
                    var xmlStep = mk61Program.getMemory().getCell().get(index);
                    memory[index] = xmlStep.getOpCode();
                }

                steps = Decoder.decodeMemory(memory);
            }

            List<ProgramRegister> registers = mk61Program.getRegisters() == null ?
                    List.of()
                    : mk61Program.getRegisters().getRegister().stream()
                    .map(LibraryConverter::convert)
                    .toList();

            return new Program(
                    new ProgramInfo(
                            mk61Program.getTitle(),
                            mk61Program.getAuthor(),
                            mk61Program.getSource(),
                            mk61Program.getDescription()
                    ),
                    steps,
                    registers
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void saveProgram(Program program, OutputStream outputStream) throws Exception {
        var mk61Program = OBJECT_FACTORY.createMk61Program();
        mk61Program.setTitle(program.info().title());
        mk61Program.setAuthor(program.info().author());
        mk61Program.setSource(program.info().source());
        mk61Program.setDescription(program.info().description());

        var mk61Memory = OBJECT_FACTORY.createMk61Memory();

        var programSteps = program.cells();
        for (int index = 0; index < programSteps.size(); index++) {
            mk61Memory.getCell().add(LibraryConverter.convert(programSteps.get(index), index));
        }

        var mk61Registers = OBJECT_FACTORY.createMk61Registers();
        var programRegisters = program.registers();
        for (int index = 0; index < programRegisters.size(); index++) {
            mk61Registers.getRegister().add(LibraryConverter.convert(programRegisters.get(index), index));
        }

        mk61Program.setMemory(mk61Memory);
        mk61Program.setRegisters(mk61Registers);

        var root = OBJECT_FACTORY.createProgram(mk61Program);

        var context = JAXBContext.newInstance(Mk61Program.class);
        var marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(root, outputStream);
    }
}

