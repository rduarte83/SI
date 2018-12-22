package License;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

class DadosMaquinaDeserializer extends StdDeserializer<DadosMaquina> {

    public DadosMaquinaDeserializer() {
        this(null);
    }

    public DadosMaquinaDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DadosMaquina deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        //System.out.println(node.toString());
        String mb =  node.get("mb").asText();
        String cpu =  node.get("cpu").asText();
        String mac = node.get("mac").asText();
        String gc =  node.get("gc").asText();

        return new DadosMaquina(mb, cpu, mac, gc);
    }
}

@JsonDeserialize(using = DadosMaquinaDeserializer.class)
public class DadosMaquina {
    private String mb;
    private String cpu;
    private String mac;
    private String gc;

    // Construtor

    public DadosMaquina(String mb, String cpu, String mac, String gc) {
        this.mb = mb;
        this.cpu = cpu;
        this.mac = mac;
        this.gc = gc;
    }

    public String getMb() {
        return mb;
    }

    public void setMb(String mb) {
        this.mb = mb;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGc() {
        return gc;
    }

    public void setGc(String gc) {
        this.gc = gc;
    }
}
