package com.jobmatcher.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.classpath.operations.jsr303.RooUploadedFile;

@Persistent
@RooJavaBean
@RooToString
@RooMongoEntity
@RooJson(deepSerialize = true)
public class CoverLetter {

    @NotNull
    @ManyToOne
    private JobSeeker jobSeeker;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @RooUploadedFile(contentType = "application/pdf")
    @Lob
    private byte[] file;

	public String toJson() {
        return new JSONSerializer().exclude("*.class").deepSerialize(this);
    }

	public static CoverLetter fromJsonToCoverLetter(String json) {
        return new JSONDeserializer<CoverLetter>().use(null, CoverLetter.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CoverLetter> collection) {
        return new JSONSerializer().exclude("*.class").deepSerialize(collection);
    }

	public static Collection<CoverLetter> fromJsonArrayToCoverLetters(String json) {
        return new JSONDeserializer<List<CoverLetter>>().use(null, ArrayList.class).use("values", CoverLetter.class).deserialize(json);
    }

	@Id
    private BigInteger id;

	public BigInteger getId() {
        return this.id;
    }

	public void setId(BigInteger id) {
        this.id = id;
    }

	public JobSeeker getJobSeeker() {
        return this.jobSeeker;
    }

	public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public byte[] getFile() {
        return this.file;
    }

	public void setFile(byte[] file) {
        this.file = file;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
