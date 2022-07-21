package de.permission.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray

class CollectionSerializer<E>(private val elementSerializer: KSerializer<E>) : KSerializer<MutableCollection<E>> {

    private val listSerializer = SetSerializer(elementSerializer)
    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: MutableCollection<E>) {
        listSerializer.serialize(encoder, value.toCollection(mutableSetOf()))
    }

    override fun deserialize(decoder: Decoder): MutableCollection<E> = with(decoder as JsonDecoder) {
        decodeJsonElement().jsonArray.mapNotNull {
            try {
                json.decodeFromJsonElement(elementSerializer, it)
            } catch (e: SerializationException) {
                e.printStackTrace()
                null
            }
        }.toCollection(mutableSetOf())
    }
}