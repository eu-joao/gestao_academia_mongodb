const academia = db.getSiblingDB("gestao_academia");

const stringType = { bsonType: "string" };
const objectIdType = { bsonType: "objectId" };
const decimalType = { bsonType: "decimal" };
const dateType = { bsonType: "date" };
const intType = { bsonType: "int" };

const collections = [
    {
        name: "unidades",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["nome", "endereco", "cnpj", "telefone"],
                properties: {
                    nome: stringType,
                    endereco: {
                        bsonType: "object",
                        required: ["logradouro", "numero", "cidade", "estado", "cep"],
                        properties: {
                            logradouro: stringType,
                            numero: stringType,
                            complemento: stringType,
                            bairro: stringType,
                            cidade: stringType,
                            estado: stringType,
                            cep: stringType
                        }
                    },
                    cnpj: { bsonType: "string", pattern: "^[0-9]{14}$" },
                    telefone: stringType
                }
            }
        }
    },
    {
        name: "alunos",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: [
                    "nome", "cpf", "telefone", "email", "dataNascimento",
                    "peso", "altura", "nivelExperiencia", "frequenciaSemanal", "status"
                ],
                properties: {
                    nome: stringType,
                    cpf: { bsonType: "string", pattern: "^[0-9]{11}$" },
                    telefone: stringType,
                    email: stringType,
                    dataNascimento: dateType,
                    peso: decimalType,
                    altura: decimalType,
                    nivelExperiencia: { enum: ["INICIANTE", "INTERMEDIARIO", "AVANCADO"] },
                    frequenciaSemanal: { bsonType: "int", enum: [3, 4, 5, 6] },
                    status: { enum: ["ATIVO", "INATIVO"] }
                }
            }
        }
    },
    {
        name: "planos",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["tipo", "valor", "duracaoDias", "percentualDesconto", "descricao"],
                properties: {
                    tipo: { enum: ["DIARIO", "MENSAL", "TRIMESTRAL", "SEMESTRAL", "ANUAL"] },
                    valor: decimalType,
                    duracaoDias: intType,
                    percentualDesconto: decimalType,
                    descricao: stringType
                }
            }
        }
    },
    {
        name: "exercicios",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["nome", "musculaturaAlvo"],
                properties: {
                    nome: stringType,
                    musculaturaAlvo: stringType
                }
            }
        }
    },
    {
        name: "programasTreino",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: [
                    "nivelExperiencia", "frequenciaSemanal", "tipoEstrutural", "descricao", "dias"
                ],
                properties: {
                    nivelExperiencia: { enum: ["INICIANTE", "INTERMEDIARIO", "AVANCADO"] },
                    frequenciaSemanal: { bsonType: "int", enum: [3, 4, 5, 6] },
                    tipoEstrutural: {
                        enum: ["FULLBODY", "UPPER_LOWER", "PPL_UPPER_LOWER", "PPL_2X"]
                    },
                    descricao: stringType,
                    dias: {
                        bsonType: "array",
                        items: {
                            bsonType: "object",
                            required: ["nomeDia", "ordem", "exercicios"],
                            properties: {
                                nomeDia: stringType,
                                ordem: intType,
                                exercicios: {
                                    bsonType: "array",
                                    items: {
                                        bsonType: "object",
                                        required: [
                                            "exercicioId", "series", "repeticoes", "ordem"
                                        ],
                                        properties: {
                                            exercicioId: objectIdType,
                                            series: intType,
                                            repeticoes: intType,
                                            ordem: intType
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    },
    {
        name: "contratos",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: [
                    "alunoId", "unidadeId", "planoId", "planoSnapshot",
                    "dataInicio", "dataTermino", "status"
                ],
                properties: {
                    alunoId: objectIdType,
                    unidadeId: objectIdType,
                    planoId: objectIdType,
                    planoSnapshot: {
                        bsonType: "object",
                        required: ["tipo", "valor", "duracaoDias", "percentualDesconto"],
                        properties: {
                            tipo: stringType,
                            valor: decimalType,
                            duracaoDias: intType,
                            percentualDesconto: decimalType
                        }
                    },
                    dataInicio: dateType,
                    dataTermino: dateType,
                    status: { enum: ["ATIVO", "CANCELADO"] }
                }
            }
        }
    },
    {
        name: "cobrancas",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["contratoId", "dataVencimento", "valor", "status"],
                properties: {
                    contratoId: objectIdType,
                    dataVencimento: dateType,
                    valor: decimalType,
                    dataPagamento: { bsonType: ["date", "null"] },
                    formaPagamento: { bsonType: ["string", "null"] },
                    status: { enum: ["PENDENTE", "PAGO", "ATRASADO"] }
                }
            }
        }
    },
    {
        name: "treinosAtivos",
        validator: {
            $jsonSchema: {
                bsonType: "object",
                required: ["alunoId", "programaTreinoId", "dataInicio", "status"],
                properties: {
                    alunoId: objectIdType,
                    programaTreinoId: objectIdType,
                    dataInicio: dateType,
                    dataFim: { bsonType: ["date", "null"] },
                    status: { enum: ["ATIVO", "ENCERRADO"] }
                }
            }
        }
    }
];

const collectionNames = new Set(academia.getCollectionNames());
for (const collection of collections) {
    const options = {
        validator: collection.validator,
        validationLevel: "strict",
        validationAction: "error"
    };

    if (collectionNames.has(collection.name)) {
        academia.runCommand({ collMod: collection.name, ...options });
    } else {
        academia.createCollection(collection.name, options);
    }
}

const indexes = [
    ["unidades", { cnpj: 1 }, { name: "uq_unidades_cnpj", unique: true }],
    ["alunos", { cpf: 1 }, { name: "uq_alunos_cpf", unique: true }],
    ["alunos", { email: 1 }, { name: "uq_alunos_email", unique: true }],
    ["planos", { tipo: 1 }, { name: "uq_planos_tipo", unique: true }],
    ["exercicios", { nome: 1 }, { name: "uq_exercicios_nome", unique: true }],
    [
        "programasTreino",
        { nivelExperiencia: 1, frequenciaSemanal: 1 },
        { name: "uq_programas_perfil", unique: true }
    ],
    ["contratos", { alunoId: 1, status: 1 }, { name: "ix_contratos_aluno_status" }],
    ["contratos", { unidadeId: 1, dataInicio: -1 }, { name: "ix_contratos_unidade_inicio" }],
    [
        "cobrancas",
        { contratoId: 1, dataVencimento: 1 },
        { name: "uq_cobrancas_contrato_vencimento", unique: true }
    ],
    ["cobrancas", { status: 1, dataVencimento: 1 }, { name: "ix_cobrancas_status_vencimento" }],
    [
        "treinosAtivos",
        { alunoId: 1 },
        {
            name: "uq_treino_ativo_por_aluno",
            unique: true,
            partialFilterExpression: { status: "ATIVO" }
        }
    ],
    ["treinosAtivos", { alunoId: 1, dataInicio: -1 }, { name: "ix_treinos_aluno_inicio" }]
];

for (const [collectionName, keys, options] of indexes) {
    academia.getCollection(collectionName).createIndex(keys, options);
}

const ids = {
    unidade: ObjectId("66a000000000000000000001"),
    plano: ObjectId("66a000000000000000000002"),
    agachamento: ObjectId("66a000000000000000000003"),
    supino: ObjectId("66a000000000000000000004"),
    remada: ObjectId("66a000000000000000000005"),
    programa: ObjectId("66a000000000000000000006"),
    aluno: ObjectId("66a000000000000000000007"),
    contrato: ObjectId("66a000000000000000000008"),
    cobranca: ObjectId("66a000000000000000000009"),
    treino: ObjectId("66a00000000000000000000a")
};

const dataInicioDemonstracao = new Date();
dataInicioDemonstracao.setUTCHours(0, 0, 0, 0);
const dataTerminoDemonstracao = new Date(dataInicioDemonstracao);
dataTerminoDemonstracao.setUTCDate(dataTerminoDemonstracao.getUTCDate() + 30);

const exemplos = [
    ["unidades", {
        _id: ids.unidade,
        nome: "Unidade de demonstração",
        endereco: {
            logradouro: "Rua Exemplo",
            numero: "100",
            bairro: "Centro",
            cidade: "Esperança",
            estado: "PB",
            cep: "58135-000"
        },
        cnpj: "00000000000000",
        telefone: "5500000000000"
    }],
    ["planos", {
        _id: ids.plano,
        tipo: "MENSAL",
        valor: Decimal128.fromString("120.00"),
        duracaoDias: NumberInt(30),
        percentualDesconto: Decimal128.fromString("0.00"),
        descricao: "Plano mensal de demonstração"
    }],
    ["exercicios", {
        _id: ids.agachamento,
        nome: "Agachamento livre",
        musculaturaAlvo: "Quadríceps"
    }],
    ["exercicios", {
        _id: ids.supino,
        nome: "Supino reto",
        musculaturaAlvo: "Peitoral"
    }],
    ["exercicios", {
        _id: ids.remada,
        nome: "Remada curvada",
        musculaturaAlvo: "Costas"
    }],
    ["programasTreino", {
        _id: ids.programa,
        nivelExperiencia: "INICIANTE",
        frequenciaSemanal: NumberInt(3),
        tipoEstrutural: "FULLBODY",
        descricao: "Programa Fullbody iniciante de demonstração",
        dias: [
            {
                nomeDia: "Treino A",
                ordem: NumberInt(1),
                exercicios: [
                    { exercicioId: ids.agachamento, series: NumberInt(3), repeticoes: NumberInt(10), ordem: NumberInt(1) },
                    { exercicioId: ids.supino, series: NumberInt(3), repeticoes: NumberInt(10), ordem: NumberInt(2) }
                ]
            },
            {
                nomeDia: "Treino B",
                ordem: NumberInt(2),
                exercicios: [
                    { exercicioId: ids.remada, series: NumberInt(3), repeticoes: NumberInt(10), ordem: NumberInt(1) },
                    { exercicioId: ids.agachamento, series: NumberInt(3), repeticoes: NumberInt(10), ordem: NumberInt(2) }
                ]
            },
            {
                nomeDia: "Treino C",
                ordem: NumberInt(3),
                exercicios: [
                    { exercicioId: ids.supino, series: NumberInt(3), repeticoes: NumberInt(10), ordem: NumberInt(1) },
                    { exercicioId: ids.remada, series: NumberInt(3), repeticoes: NumberInt(10), ordem: NumberInt(2) }
                ]
            }
        ]
    }],
    ["alunos", {
        _id: ids.aluno,
        nome: "Aluno Demonstração",
        cpf: "00000000000",
        telefone: "5500000000000",
        email: "aluno.demo@example.test",
        dataNascimento: ISODate("2000-01-01T00:00:00Z"),
        peso: Decimal128.fromString("70.00"),
        altura: Decimal128.fromString("1.75"),
        nivelExperiencia: "INICIANTE",
        frequenciaSemanal: NumberInt(3),
        status: "ATIVO"
    }],
    ["contratos", {
        _id: ids.contrato,
        alunoId: ids.aluno,
        unidadeId: ids.unidade,
        planoId: ids.plano,
        planoSnapshot: {
            tipo: "MENSAL",
            valor: Decimal128.fromString("120.00"),
            duracaoDias: NumberInt(30),
            percentualDesconto: Decimal128.fromString("0.00")
        },
        dataInicio: dataInicioDemonstracao,
        dataTermino: dataTerminoDemonstracao,
        status: "ATIVO"
    }],
    ["cobrancas", {
        _id: ids.cobranca,
        contratoId: ids.contrato,
        dataVencimento: dataInicioDemonstracao,
        valor: Decimal128.fromString("120.00"),
        dataPagamento: null,
        formaPagamento: null,
        status: "PENDENTE"
    }],
    ["treinosAtivos", {
        _id: ids.treino,
        alunoId: ids.aluno,
        programaTreinoId: ids.programa,
        dataInicio: dataInicioDemonstracao,
        dataFim: null,
        status: "ATIVO"
    }]
];

for (const [collectionName, documento] of exemplos) {
    academia.getCollection(collectionName).updateOne(
        { _id: documento._id },
        { $setOnInsert: documento },
        { upsert: true }
    );
}

assert.eq(1, academia.alunos.countDocuments({ _id: ids.aluno }));
assert.eq(3, academia.programasTreino.findOne({ _id: ids.programa }).dias.length);
assert.eq(1, academia.cobrancas.countDocuments({ contratoId: ids.contrato }));
assert.eq(
    1,
    academia.treinosAtivos.countDocuments({ alunoId: ids.aluno, status: "ATIVO" })
);

print("Modelo documental gestao_academia criado e validado com sucesso.");
