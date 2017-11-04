package cc.vileda.openapi.dsl

import com.reprezen.kaizen.oasparser.OpenApi3Parser
import io.swagger.converter.ModelConverters
import io.swagger.oas.models.*
import io.swagger.oas.models.examples.Example
import io.swagger.oas.models.info.Info
import io.swagger.oas.models.media.*
import io.swagger.oas.models.parameters.Parameter
import io.swagger.oas.models.parameters.RequestBody
import io.swagger.oas.models.responses.ApiResponse
import io.swagger.oas.models.responses.ApiResponses
import io.swagger.oas.models.security.SecurityRequirement
import io.swagger.oas.models.security.SecurityScheme
import io.swagger.oas.models.servers.Server
import io.swagger.oas.models.servers.ServerVariable
import io.swagger.oas.models.servers.ServerVariables
import io.swagger.oas.models.tags.Tag
import io.swagger.util.Json
import org.json.JSONObject
import java.io.File
import java.nio.file.Files

fun openapiDsl(init: OpenAPI.() -> Unit): OpenAPI {
    val openapi3 = OpenAPI()
    openapi3.init()
    return openapi3
}

internal fun validatedJson(api: OpenAPI): JSONObject {
    val json = Json.mapper().writeValueAsString(api)
    OpenApi3Parser().parse(toFile(json), true)
    return JSONObject(json)
}

fun OpenAPI.asJson(): JSONObject {
    return validatedJson(this)
}

fun OpenAPI.asFile(): File {
    return toFile(asJson().toString(2))
}

private fun toFile(json: String): File {
    val file = Files.createTempFile("openapi-", ".json").toFile()
    file.writeText(json)
    file.deleteOnExit()
    return file
}

fun OpenAPI.info(init: Info.() -> Unit) {
    info = Info()
    info.init()
}

fun OpenAPI.externalDocs(init: ExternalDocumentation.() -> Unit) {
    externalDocs = ExternalDocumentation()
    externalDocs.init()
}

fun OpenAPI.server(init: Server.() -> Unit) {
    val server = Server()
    server.init()
    if (servers == null)
        servers = mutableListOf()
    servers.add(server)
}

fun OpenAPI.security(init: SecurityRequirement.() -> Unit) {
    val securityReq = SecurityRequirement()
    securityReq.init()
    if (security == null)
        security = mutableListOf()
    security.add(securityReq)
}

fun OpenAPI.tag(init: Tag.() -> Unit) {
    val tag = Tag()
    tag.init()
    if (tags == null)
        tags = mutableListOf()
    tags.add(tag)
}

fun OpenAPI.paths(init: Paths.() -> Unit) {
    paths = Paths()
    paths.init()
}

private fun OpenAPI.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun OpenAPI.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

private fun PathItem.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun PathItem.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

private fun Operation.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun Operation.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

private fun Components.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun Components.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

private fun ApiResponse.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun ApiResponse.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

fun OpenAPI.components(init: Components.() -> Unit) {
    if (components == null)
        components = Components()
    components.init()
}

inline fun <reified T> Components.schema(init: Schema<*>.() -> Unit) {
    if (schemas == null) {
        schemas = mutableMapOf()
    }
    val schema = findSchema<T>()
    schema!!.init()
    schemas.put(T::class.java.simpleName, schema)
}

inline fun <reified T> Components.schema() {
    if (schemas == null) {
        schemas = mutableMapOf()
    }
    val schema = findSchema<T>()
    schemas.put(T::class.java.simpleName, schema)
}

fun Components.securityScheme(init: SecurityScheme.() -> Unit) {
    val security = SecurityScheme()
    security.init()
    if (securitySchemes == null) {
        securitySchemes = mutableMapOf()
    }
    securitySchemes.put(security.name, security)
}

private fun Tag.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun Tag.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

fun Paths.path(name: String, init: PathItem.() -> Unit) {
    val pathItem = PathItem()
    pathItem.init()
    addPathItem(name, pathItem)
}

private fun Paths.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun Paths.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

fun PathItem.get(init: Operation.() -> Unit) {
    get = Operation()
    get.init()
}

fun PathItem.put(init: Operation.() -> Unit) {
    put = Operation()
    put.init()
}

fun PathItem.post(init: Operation.() -> Unit) {
    post = Operation()
    post.init()
}

fun PathItem.delete(init: Operation.() -> Unit) {
    delete = Operation()
    delete.init()
}

fun PathItem.patch(init: Operation.() -> Unit) {
    patch = Operation()
    patch.init()
}

fun PathItem.options(init: Operation.() -> Unit) {
    options = Operation()
    options.init()
}

fun PathItem.head(init: Operation.() -> Unit) {
    head = Operation()
    head.init()
}

fun PathItem.trace(init: Operation.() -> Unit) {
    trace = Operation()
    trace.init()
}

fun Operation.responses(init: ApiResponses.() -> Unit) {
    responses = ApiResponses()
    responses.init()
}

fun Operation.requestBody(init: RequestBody.() -> Unit) {
    requestBody = RequestBody()
    requestBody.init()
}

fun Operation.parameter(init: Parameter.() -> Unit) {
    if (parameters == null) {
        parameters = mutableListOf()
    }
    val parameter = Parameter()
    parameter.init()
    parameters.add(parameter)
}

fun ApiResponses.response(name: String, init: ApiResponse.() -> Unit) {
    val response = ApiResponse()
    response.init()
    put(name, response)
}

fun ApiResponse.content(init: Content.() -> Unit) {
    content = Content()
    content.init()
}

fun RequestBody.content(init: Content.() -> Unit) {
    content = Content()
    content.init()
}

private fun RequestBody.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun RequestBody.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

private fun Parameter.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun Parameter.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

private fun ExternalDocumentation.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun ExternalDocumentation.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

fun Parameter.content(init: Content.() -> Unit) {
    content = Content()
    content.init()
}

inline fun <reified T> Parameter.schema(init: Schema<*>.() -> Unit) {
    schema = findSchema<T>()
    schema.init()
}

inline fun <reified T> Parameter.schema() {
    schema = findSchema<T>()
}

private fun <T> Schema<T>.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun <T> Schema<T>.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

inline fun <reified T> mediaType(): MediaType {
    val mediaType = MediaType()
    val modelSchema = findSchema<T>()
    mediaType.schema = modelSchema
    return mediaType
}

inline fun <reified T> mediaTypeRef(): MediaType {
    val mediaType = MediaType()
    mediaType.schema = Schema<T>()
    mediaType.schema.`$ref` = T::class.java.simpleName
    return mediaType
}

inline fun <reified T> Content.mediaType(name: String, init: MediaType.() -> Unit) {
    val mediaType = mediaType<T>()
    mediaType.init()
    addMediaType(name, mediaType)
}

inline fun <reified T> Content.mediaTypeRef(name: String, init: MediaType.() -> Unit) {
    val mediaType = mediaTypeRef<T>()
    mediaType.init()
    addMediaType(name, mediaType)
}

inline fun <reified T> Content.mediaTypeRef(name: String) {
    val mediaType = mediaTypeRef<T>()
    addMediaType(name, mediaType)
}

inline fun <reified T> Content.mediaType(name: String) {
    val mediaType = mediaType<T>()
    addMediaType(name, mediaType)
}

inline fun <reified T> findSchema(): Schema<*>? {
    return when (T::class.java) {
        String::class.java -> StringSchema()
        Boolean::class.java -> BooleanSchema()
        Int::class.java -> IntegerSchema()
        else -> ModelConverters.getInstance().read(T::class.java)[T::class.java.simpleName]
    }
}

private fun MediaType.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun MediaType.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

inline fun <reified T> MediaType.example(value: T, init: Example.() -> Unit) {
    if (examples == null) {
        examples = mutableMapOf()
    }

    val example = Example()
    example.value = value
    example.init()
    examples.put(T::class.java.simpleName, example)
}

fun Server.variables(init: ServerVariables.() -> Unit) {
    variables = ServerVariables()
    variables.init()
}

private fun Server.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun Server.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}

fun ServerVariables.variable(name: String, init: ServerVariable.() -> Unit) {
    val serverVariable = ServerVariable()
    serverVariable.init()
    addServerVariable(name, serverVariable)
}

private fun ServerVariables.extensions(init: MutableMap<String, Any>.() -> Unit) {
    if (extensions == null)
        extensions = mutableMapOf()
    extensions.init()
}

fun ServerVariables.extension(name: String, value: Any) {
    extensions {
        put(name, value)
    }
}
